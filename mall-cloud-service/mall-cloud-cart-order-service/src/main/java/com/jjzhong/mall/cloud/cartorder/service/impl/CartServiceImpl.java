package com.jjzhong.mall.cloud.cartorder.service.impl;

import com.jjzhong.mall.cloud.cartorder.constant.CartSelectStatusEnum;
import com.jjzhong.mall.cloud.common.model.vo.CommonResponse;
import com.jjzhong.mall.cloud.cartorder.exception.MallCartOrderServiceException;
import com.jjzhong.mall.cloud.cartorder.exception.MallCartOrderServiceExceptionEnum;
import com.jjzhong.mall.cloud.cartorder.feign.ProductClient;
import com.jjzhong.mall.cloud.cartorder.model.dao.CartMapper;
import com.jjzhong.mall.cloud.cartorder.model.pojo.Cart;
import com.jjzhong.mall.cloud.cartorder.model.vo.CartVO;
import com.jjzhong.mall.cloud.cartorder.service.CartService;
import com.jjzhong.mall.cloud.common.exception.MallCommonException;
import com.jjzhong.mall.cloud.common.exception.MallCommonExceptionEnum;
import com.jjzhong.mall.cloud.filter.AccessContext;
import com.jjzhong.mall.cloud.sdk.product.vo.ProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 购物车服务
 */
@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductClient productClient;

    /**
     * 用户购物车列表
     * @return 购物车VO列表
     */
    @Override
    public List<CartVO> list() {
        Integer userId = AccessContext.getCommonUserInfo().getId();
        List<CartVO> cartVOs = cartMapper.selectListByUserId(userId);
        cartVOs.forEach(cartVO -> cartVO.setTotalPrice(cartVO.getPrice() * cartVO.getQuantity()));
        return cartVOs;
    }

    /**
     * 用户往购物车添加商品
     * @param productId 商品 id
     * @param count 商品数量
     * @return 购物车VO列表
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<CartVO> add(Integer productId, Integer count) {
        Integer userId = AccessContext.getCommonUserInfo().getId();
        validateProduct(productId, count);
        Cart cart = cartMapper.selectByUserIdAndProductId(userId, productId);
        if (cart == null) {
            Cart newCart = new Cart();
            newCart.setUserId(userId);
            newCart.setProductId(productId);
            newCart.setQuantity(count);
            newCart.setSelected(CartSelectStatusEnum.SELECTED.getStatus());
            cartMapper.insertSelective(newCart);
        } else {
            updateCount(cart, cart.getQuantity() + count);
        }
        return this.list();
    }

    /**
     * 更新商品数量
     * @param productId 商品 id
     * @param count 商品数量
     * @return 购物车VO列表
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<CartVO> updateCount(Integer productId, Integer count) {
        Integer userId = AccessContext.getCommonUserInfo().getId();
        validateProduct(productId, count);
        Cart cart = cartMapper.selectByUserIdAndProductId(userId, productId);
        if (cart == null) {
            throw new MallCommonException(MallCommonExceptionEnum.UPDATE_FAILED);
        } else {
            updateCount(cart, count);
        }
        return this.list();
    }

    /**
     * 更新购物车选择状态
     * @param productId 商品 id
     * @param selectStatus 商品选择状态
     * @return 购物车VO列表
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<CartVO> updateSelect(Integer productId, Integer selectStatus) {
        Integer userId = AccessContext.getCommonUserInfo().getId();
        Cart cart = cartMapper.selectByUserIdAndProductId(userId, productId);
        if (cart == null) {
            throw new MallCommonException(MallCommonExceptionEnum.UPDATE_FAILED);
        } else {
            cartMapper.updateSelectStatus(userId, productId, CartSelectStatusEnum.of(selectStatus).getStatus());
        }
        return this.list();
    }

    /**
     * 更新购物车中所有商品的选择状态
     * @param selectStatus 商品选择状态
     * @return 购物车VO列表
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<CartVO> updateSelectAll(Integer selectStatus) {
        Integer userId = AccessContext.getCommonUserInfo().getId();
        selectStatus = CartSelectStatusEnum.of(selectStatus).getStatus();
        cartMapper.updateSelectStatus(userId, null, selectStatus);
        return this.list();
    }

    /**
     * 删除购物车中的商品
     * @param productId 商品选择状态
     * @return 购物车VO列表
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<CartVO> delete(Integer productId) {
        Integer userId = AccessContext.getCommonUserInfo().getId();
        int cnt = cartMapper.deleteByUserIdAndProductId(userId, productId);
        if (cnt == 0) {
            throw new MallCommonException(MallCommonExceptionEnum.DELETE_FAILED);
        }
        return this.list();
    }

    /**
     * 校验购物车中的商品（是否上架，库存是否充足）
     * @param productId 商品选择状态
     * @param count 商品数量
     */
    public void validateProduct(Integer productId, Integer count) {
        CommonResponse<ProductVO> response = productClient.detail(productId);
        if (response.isError() || response.getData() == null)
            throw new MallCartOrderServiceException(response.getStatus(), response.getMsg());
        else {
            ProductVO productVO = response.getData();
            if (productVO.getStock() < count)
                throw new MallCartOrderServiceException(MallCartOrderServiceExceptionEnum.STOCK_NOT_ENOUGH);
        }
    }

    /**
     * 更新购物车中的商品数量
     * @param cartOld 原购物车中的商品
     * @param count 更新的数量
     */
    private void updateCount(Cart cartOld, Integer count) {
        validateProduct(cartOld.getProductId(), count);
        Cart newCart = new Cart();
        newCart.setQuantity(count);
        newCart.setId(cartOld.getId());
        newCart.setSelected(CartSelectStatusEnum.SELECTED.getStatus());
        newCart.setProductId(cartOld.getProductId());
        newCart.setUserId(cartOld.getUserId());
        cartMapper.updateByPrimaryKeySelective(newCart);
    }

}
