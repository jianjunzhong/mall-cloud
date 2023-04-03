package com.jjzhong.mall.cloud.cartorder.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jjzhong.mall.cloud.cartorder.constant.CartSelectStatusEnum;
import com.jjzhong.mall.cloud.cartorder.constant.OrderStatusEnum;
import com.jjzhong.mall.cloud.cartorder.exception.MallCartOrderServiceException;
import com.jjzhong.mall.cloud.cartorder.exception.MallCartOrderServiceExceptionEnum;
import com.jjzhong.mall.cloud.cartorder.feign.ProductClient;
import com.jjzhong.mall.cloud.cartorder.model.dao.CartMapper;
import com.jjzhong.mall.cloud.cartorder.model.dao.OrderItemMapper;
import com.jjzhong.mall.cloud.cartorder.model.dao.OrderMapper;
import com.jjzhong.mall.cloud.cartorder.model.pojo.Order;
import com.jjzhong.mall.cloud.cartorder.model.pojo.OrderItem;
import com.jjzhong.mall.cloud.cartorder.model.query.OrderStatisticsQuery;
import com.jjzhong.mall.cloud.cartorder.model.request.CreateOrderReq;
import com.jjzhong.mall.cloud.cartorder.model.vo.CartVO;
import com.jjzhong.mall.cloud.cartorder.model.vo.OrderItemVO;
import com.jjzhong.mall.cloud.cartorder.model.vo.OrderStatisticsVO;
import com.jjzhong.mall.cloud.cartorder.model.vo.OrderVO;
import com.jjzhong.mall.cloud.cartorder.service.CartService;
import com.jjzhong.mall.cloud.cartorder.service.OrderService;
import com.jjzhong.mall.cloud.cartorder.stream.RestoreStockSendService;
import com.jjzhong.mall.cloud.cartorder.util.OrderNumFactory;
import com.jjzhong.mall.cloud.common.constant.CommonConstant;
import com.jjzhong.mall.cloud.common.model.vo.CommonResponse;
import com.jjzhong.mall.cloud.common.model.vo.CommonUserInfo;
import com.jjzhong.mall.cloud.filter.AccessContext;
import com.jjzhong.mall.cloud.sdk.product.message.RestoreStockMsg;
import io.seata.spring.annotation.GlobalTransactional;
import com.jjzhong.mall.cloud.sdk.product.dto.DeductStockDTO;
import com.jjzhong.mall.cloud.sdk.product.request.DeductStockReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 订单服务
 */
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private CartService cartService;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private ProductClient productClient;

    /** Stream 消息服务 */
    @Autowired
    private RestoreStockSendService restoreStockSendService;

    /**
     * 创建订单
     * @param createOrderReq 创建订单请求
     * @return 订单号
     */
    @GlobalTransactional(rollbackFor = Exception.class)
    @Override
    public String create(CreateOrderReq createOrderReq) {
        Integer userId = AccessContext.getCommonUserInfo().getId();
        // 获取购物车中已选择的商品并删除
        List<CartVO> cartVOs = cartService.list();
        List<CartVO> cartVOSelected = cartVOs.stream()
                .filter(item -> {
                    if (item.getSelected().equals(CartSelectStatusEnum.SELECTED.getStatus())) {
                        cartMapper.deleteByPrimaryKey(item.getId());
                        return true;
                    }
                    return false;
                })
                .collect(Collectors.toList());
        // 如果没有已选择的商品，则抛出异常
        if (CollectionUtils.isEmpty(cartVOSelected))
            throw new MallCartOrderServiceException(MallCartOrderServiceExceptionEnum.CART_EMPTY_OR_NOT_SELECTED);
        // 向商品服务发送请求，扣减库存
        List<DeductStockDTO> deductStockDTOs = cartVOSelected.stream()
                .map(cartVO -> new DeductStockDTO(cartVO.getProductId(), cartVO.getQuantity()))
                .collect(Collectors.toList());
        DeductStockReq deductStockReq = new DeductStockReq(deductStockDTOs);
        CommonResponse<Object> response = productClient.deductStock(deductStockReq);
        // 若出错（如商品不存在或下架或库存不足，则抛出异常）
        if (response.isError()) {
            throw new MallCartOrderServiceException(response.getStatus(), response.getMsg());
        }
        // 将 CartVO 转换为 OrderItem
        List<OrderItem> orderItems = cartVOToOrderItem(cartVOSelected);
        // 创建订单
        String orderNo = OrderNumFactory.getOrderNo();
        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setTotalPrice(calculateTotalPrice(cartVOSelected));
        order.setReceiverName(createOrderReq.getReceiverName());
        order.setReceiverMobile(createOrderReq.getReceiverMobile());
        order.setReceiverAddress(createOrderReq.getReceiverAddress());
        order.setOrderStatus(OrderStatusEnum.NOT_PAY.getCode());
        order.setPostage(0);
        order.setPaymentType(1);
        orderMapper.insertSelective(order);
        // 将订单中的所有商品写入 order_item 表中
        for (OrderItem orderItem : orderItems) {
            orderItem.setOrderNo(orderNo);
            orderItemMapper.insertSelective(orderItem);
        }
        return orderNo;
    }

    /**
     * 查询订单
     * @param orderNo 订单号
     * @return
     */
    @Override
    public OrderVO detail(String orderNo) {
        AccessContext.getCommonUserInfo();
        Order order = orderMapper.selectByOrderNo(orderNo);
        // 验证订单所属
        validateOrder(AccessContext.getCommonUserInfo(), order);
        return orderToOrderVO(order);
    }

    /**
     * 查询普通用户订单列表
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return 分页后的订单 VO 列表
     */
    @Override
    public PageInfo<OrderVO> listForCustomer(Integer pageNum, Integer pageSize) {
        Integer userId = AccessContext.getCommonUserInfo().getId();
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orders = orderMapper.selectByUserId(userId);
        List<OrderVO> orderVOs = new ArrayList<>();
        for (Order order : orders) {
            orderVOs.add(orderToOrderVO(order));
        }
        return new PageInfo<>(orderVOs);
    }

    /**
     * 查询管理员订单列表
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return 分页后的订单 VO 列表
     */
    @Override
    public PageInfo<OrderVO> listForAdmin(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orders = orderMapper.selectList();
        List<OrderVO> orderVOs = new ArrayList<>();
        for (Order order : orders) {
            orderVOs.add(orderToOrderVO(order));
        }
        return new PageInfo<>(orderVOs);
    }

    /**
     * 取消订单
     * @param orderNo 订单号
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void cancel(String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        // 查询订单所属
        validateOrder(AccessContext.getCommonUserInfo(), order);
        if (order.getOrderStatus().equals(OrderStatusEnum.NOT_PAY.getCode())) {
            order.setUpdateTime(null);
            order.setCreateTime(null);
            order.setEndTime(new Date());
            order.setOrderStatus(OrderStatusEnum.CANCELED.getCode());
            orderMapper.updateByPrimaryKeySelective(order);

            List<OrderItem> orderItems = orderItemMapper.selectListByOrderNo(orderNo);
            for (OrderItem orderItem : orderItems) {
                restoreStockSendService.sendMessage(new RestoreStockMsg(orderItem.getProductId(), orderItem.getQuantity()));
            }
        } else if (order.getOrderStatus().equals(OrderStatusEnum.CANCELED.getCode())) {
            throw new MallCartOrderServiceException(MallCartOrderServiceExceptionEnum.ORDER_CANCELED);
        } else if (order.getOrderStatus().equals(OrderStatusEnum.PAID.getCode())) {
            throw new MallCartOrderServiceException(MallCartOrderServiceExceptionEnum.ORDER_PAID);
        } else if (order.getOrderStatus().equals(OrderStatusEnum.DELIVERED.getCode())) {
            throw new MallCartOrderServiceException(MallCartOrderServiceExceptionEnum.ORDER_DELIVERED);
        } else if (order.getOrderStatus().equals(OrderStatusEnum.FINISHED.getCode())) {
            throw new MallCartOrderServiceException(MallCartOrderServiceExceptionEnum.ORDER_FINISHED);
        } else {
            throw new MallCartOrderServiceException(MallCartOrderServiceExceptionEnum.ORDER_STATUS_INCORRECT);
        }
    }

    /**
     * 支付订单
     * @param orderNo 订单号
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void pay(String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order.getOrderStatus().equals(OrderStatusEnum.NOT_PAY.getCode())) {
            order.setOrderStatus(OrderStatusEnum.PAID.getCode());
            order.setUpdateTime(null);
            order.setCreateTime(null);
            order.setPayTime(new Date());
            orderMapper.updateByPrimaryKeySelective(order);
        } else if (order.getOrderStatus().equals(OrderStatusEnum.CANCELED.getCode())) {
            throw new MallCartOrderServiceException(MallCartOrderServiceExceptionEnum.ORDER_CANCELED);
        } else if (order.getOrderStatus().equals(OrderStatusEnum.PAID.getCode())) {
            throw new MallCartOrderServiceException(MallCartOrderServiceExceptionEnum.ORDER_PAID);
        } else if (order.getOrderStatus().equals(OrderStatusEnum.DELIVERED.getCode())) {
            throw new MallCartOrderServiceException(MallCartOrderServiceExceptionEnum.ORDER_DELIVERED);
        } else if (order.getOrderStatus().equals(OrderStatusEnum.FINISHED.getCode())) {
            throw new MallCartOrderServiceException(MallCartOrderServiceExceptionEnum.ORDER_FINISHED);
        } else {
            throw new MallCartOrderServiceException(MallCartOrderServiceExceptionEnum.ORDER_STATUS_INCORRECT);
        }
    }

    /**
     * 订单发货
     * @param orderNo 订单号
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deliver(String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        validateOrder(order);
        if (order.getOrderStatus().equals(OrderStatusEnum.PAID.getCode())) {
            order.setOrderStatus(OrderStatusEnum.DELIVERED.getCode());
            order.setUpdateTime(null);
            order.setCreateTime(null);
            order.setDeliveryTime(new Date());
            orderMapper.updateByPrimaryKeySelective(order);
        } else if (order.getOrderStatus().equals(OrderStatusEnum.CANCELED.getCode())) {
            throw new MallCartOrderServiceException(MallCartOrderServiceExceptionEnum.ORDER_CANCELED);
        } else if (order.getOrderStatus().equals(OrderStatusEnum.NOT_PAY.getCode())) {
            throw new MallCartOrderServiceException(MallCartOrderServiceExceptionEnum.ORDER_NOT_PAID);
        } else if (order.getOrderStatus().equals(OrderStatusEnum.DELIVERED.getCode())) {
            throw new MallCartOrderServiceException(MallCartOrderServiceExceptionEnum.ORDER_DELIVERED);
        } else if (order.getOrderStatus().equals(OrderStatusEnum.FINISHED.getCode())) {
            throw new MallCartOrderServiceException(MallCartOrderServiceExceptionEnum.ORDER_FINISHED);
        } else {
            throw new MallCartOrderServiceException(MallCartOrderServiceExceptionEnum.ORDER_STATUS_INCORRECT);
        }
    }

    /**
     * 完结订单
     * @param orderNo 订单号
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void finish(String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        validateOrder(AccessContext.getCommonUserInfo(), order);
        if (order.getOrderStatus().equals(OrderStatusEnum.DELIVERED.getCode())) {
            order.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
            order.setUpdateTime(null);
            order.setCreateTime(null);
            order.setEndTime(new Date());
            orderMapper.updateByPrimaryKeySelective(order);
        } else if (order.getOrderStatus().equals(OrderStatusEnum.CANCELED.getCode())) {
            throw new MallCartOrderServiceException(MallCartOrderServiceExceptionEnum.ORDER_CANCELED);
        } else if (order.getOrderStatus().equals(OrderStatusEnum.NOT_PAY.getCode())) {
            throw new MallCartOrderServiceException(MallCartOrderServiceExceptionEnum.ORDER_NOT_PAID);
        } else if (order.getOrderStatus().equals(OrderStatusEnum.PAID.getCode())) {
            throw new MallCartOrderServiceException(MallCartOrderServiceExceptionEnum.ORDER_NOT_DELIVERED);
        } else if (order.getOrderStatus().equals(OrderStatusEnum.FINISHED.getCode())) {
            throw new MallCartOrderServiceException(MallCartOrderServiceExceptionEnum.ORDER_FINISHED);
        } else {
            throw new MallCartOrderServiceException(MallCartOrderServiceExceptionEnum.ORDER_STATUS_INCORRECT);
        }
    }

    /**
     * 查询订单量数据
     * @param orderStatisticsQuery 订单数据查询
     * @return 订单数据列表
     */
    @Override
    public List<OrderStatisticsVO> statistics(OrderStatisticsQuery orderStatisticsQuery) {
        return orderMapper.selectOrderStatistics(orderStatisticsQuery);
    }

    /**
     * 验证订单是否有效
     * @param order 订单实体
     */
    private void validateOrder(Order order) {
        if (order == null) {
            throw new MallCartOrderServiceException(MallCartOrderServiceExceptionEnum.ORDER_NOT_FOUND);
        }
    }
    /**
     * 验证订单是否有效或匹配
     * @param order 订单实体
     */
    private void validateOrder(CommonUserInfo commonUserInfo, Order order) {
        validateOrder(order);
        if (!Objects.equals(commonUserInfo.getRole(), CommonConstant.Role.ADMIN.getCode())
                && !order.getUserId().equals(AccessContext.getCommonUserInfo().getId())) {
            throw new MallCartOrderServiceException(MallCartOrderServiceExceptionEnum.ORDER_NOT_MATCH);
        }
    }

    /**
     * Order 转换为 OrderVO
     * @param order 要转换的 Order
     * @return OrderVO
     */
    private OrderVO orderToOrderVO(Order order) {
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(order, orderVO);
        orderVO.setOrderStatusName(OrderStatusEnum.of(order.getOrderStatus()).getName());
        // 获取OrderItemsVOList
        List<OrderItem> orderItems = orderItemMapper.selectListByOrderNo(order.getOrderNo());
        List<OrderItemVO> orderItemVOList = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            OrderItemVO orderItemVO = new OrderItemVO();
            BeanUtils.copyProperties(orderItem, orderItemVO);
            orderItemVOList.add(orderItemVO);
        }
        orderVO.setOrderItemVOList(orderItemVOList);
        return orderVO;
    }

    /**
     * 用于下单时将购物车中的商品转换成 OrderItem
     * @param cartVOSelected 购物车中已选择的商品
     * @return OrderItem 列表
     */
    private List<OrderItem> cartVOToOrderItem(List<CartVO> cartVOSelected) {
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartVO cartVO : cartVOSelected) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(cartVO.getProductId());
            orderItem.setProductName(cartVO.getProductName());
            orderItem.setProductImg(cartVO.getProductImage());
            orderItem.setUnitPrice(cartVO.getPrice());
            orderItem.setQuantity(cartVO.getQuantity());
            orderItem.setTotalPrice(cartVO.getTotalPrice());
            orderItems.add(orderItem);
        }
        return orderItems;
    }

    /**
     * 计算购物车中已选择的商品的总价
     * @param cartVOSelected 购物车中已选择的商品
     * @return 购物车中已选择的商品的总价
     */
    private Integer calculateTotalPrice(List<CartVO> cartVOSelected) {
        Integer totalPrice = 0;
        for (CartVO cartVO : cartVOSelected) {
            totalPrice += cartVO.getTotalPrice();
        }
        return totalPrice;
    }
}
