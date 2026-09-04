package com.campus_delivery.service;

import com.campus_delivery.dto.ShoppingCartDTO;
import com.campus_delivery.entity.ShoppingCart;
import java.util.List;

public interface ShoppingCartService {

    /**
     * 添加购物车
     * @param shoppingCartDTO
     */
    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);
    /**
     * 查看购物车
     * @return
     */
    List<ShoppingCart> showShoppingCart();
    /**
     * 清空购物车商品
     */
    void cleanShoppingCart();

    void subtractShoppingCart(ShoppingCartDTO shoppingCartDTO);
}