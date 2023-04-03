package com.jjzhong.mall.cloud.cartorder.model.dao;

import com.jjzhong.mall.cloud.cartorder.model.pojo.Cart;
import com.jjzhong.mall.cloud.cartorder.model.vo.CartVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart row);

    int insertSelective(Cart row);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart row);

    int updateByPrimaryKey(Cart row);

    List<CartVO> selectListByUserId(Integer userId);

    Cart selectByUserIdAndProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);
    void updateSelectStatus(@Param("userId") Integer userId, @Param("productId") Integer productId, @Param("selectStatus") Integer selectStatus);
    int deleteByUserIdAndProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);
}