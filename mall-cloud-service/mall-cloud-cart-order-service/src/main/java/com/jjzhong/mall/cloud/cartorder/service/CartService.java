package com.jjzhong.mall.cloud.cartorder.service;

import com.jjzhong.mall.cloud.cartorder.model.vo.CartVO;

import java.util.List;

public interface CartService {
    List<CartVO> list();

    List<CartVO> add(Integer productId, Integer count);

    List<CartVO> updateCount(Integer productId, Integer count);

    List<CartVO> updateSelect(Integer productId, Integer selectStatus);

    List<CartVO> updateSelectAll(Integer selectStatus);

    List<CartVO> delete(Integer productId);
}
