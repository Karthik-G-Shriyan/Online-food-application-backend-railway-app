package com.unicksbyte.Online_food_ordering_app.service;

import com.unicksbyte.Online_food_ordering_app.entity.CartEntity;
import com.unicksbyte.Online_food_ordering_app.io.CartRequest;
import com.unicksbyte.Online_food_ordering_app.io.CartResponse;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


public interface CartService {

    public CartResponse addToCart(CartRequest request);

    public CartResponse getCart();

    public void clearCart();

    public CartResponse removeFromCart(CartRequest cartRequest);
}
