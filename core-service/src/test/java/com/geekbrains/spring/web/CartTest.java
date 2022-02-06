package com.geekbrains.spring.web;

import com.geekbrains.spring.web.core.dto.Cart;
import com.geekbrains.spring.web.core.dto.OrderItemDto;
import com.geekbrains.spring.web.core.entities.Product;
import com.geekbrains.spring.web.core.services.CartService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

@ActiveProfiles("test")
public class CartTest {
    @Autowired
    private Cart cart;

    @Autowired
    private CartService cartService;

    @Test
    public void cartFillTest() {
        for (int i = 0; i < 5; i++) {
             Product product = new Product();
             product.getId(new Long(i + 1));
             product.setPrice(new Integer(100 + 1 * 10));
             product.setTitle("Product #" + i + 1);
             cart.add(product);
         }
       Assertions.assertEquals(5,cart.getItems().size());
    }

    @Test
    public void cartMergeTest(Cart cart) {
        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setProductId(1L);
        orderItemDto.setProductTitle("Product");
        orderItemDto.setPricePerProduct(1);
        orderItemDto.getQuantity(1);
        orderItemDto.setPrice(200);

        List<OrderItemDto> orderItemDtos = new ArrayList<>();
        orderItemDtos.add(orderItemDto);

        Cart newCart = new Cart();
        newCart.setItems(orderItemDtos);
        newCart.getTotalPrice(400);

        Cart userCart = new Cart();
        cartService.clearCart("user_Cart_test");
        cartService.updateCart("user_cart_test", userCart);
        cartService.updateCart("test_Cart", newCart);
        cartService.merge("user_cart_test", "test_cart");

        Assertions.assertEquals(0, cartService.getCurrentCart("test_Cart").getItems().size());
        Assertions.assertEquals(1, cartService.getCurrentCart("test_Cart_user").getItems().size());
    }
}