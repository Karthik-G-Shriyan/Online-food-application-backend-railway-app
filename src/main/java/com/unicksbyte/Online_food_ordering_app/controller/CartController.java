package com.unicksbyte.Online_food_ordering_app.controller;


import com.unicksbyte.Online_food_ordering_app.io.CartRequest;
import com.unicksbyte.Online_food_ordering_app.io.CartResponse;
import com.unicksbyte.Online_food_ordering_app.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

@PostMapping
    public CartResponse addToCart(@RequestBody CartRequest request) {
    String foodId = request.getFoodId();

    if(foodId == null || foodId.isEmpty()){
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "Food id not found");
    }
   return cartService.addToCart(request);

}
    @GetMapping
    public CartResponse getCart(){
    return cartService.getCart();
    }


    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearCart(){
    cartService.clearCart();
    }


    @PostMapping("/remove")
    public CartResponse removeFromCart(@RequestBody CartRequest request){
        String foodId = request.getFoodId();

        if(foodId == null || foodId.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "Food id not found");
        }
        return cartService.removeFromCart(request);
    }
}
