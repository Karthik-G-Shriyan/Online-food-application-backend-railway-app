package com.unicksbyte.Online_food_ordering_app.controller;


import com.razorpay.RazorpayException;
import com.unicksbyte.Online_food_ordering_app.io.OrderRequest;
import com.unicksbyte.Online_food_ordering_app.io.OrderResponse;
import com.unicksbyte.Online_food_ordering_app.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse createOrderWithPayment(@RequestBody OrderRequest request){

        try {
            OrderResponse response = orderService.createOrderWithPayment(request);
            return  response;
        }
        catch (RazorpayException e) {
            throw new RuntimeException(e);
        }

    }

    @PostMapping("/verify")
    public void verifyPayment(@RequestBody Map<String, String> paymentData){

        orderService.verifyPayment(paymentData, "paid");

    }


    @GetMapping
    public List<OrderResponse> getOrders(){

        return  orderService.getUserOrders();
    }


    @DeleteMapping("/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public  void deleteOrder(@PathVariable String orderId){

        orderService.removeOrder(orderId);
    }




}
