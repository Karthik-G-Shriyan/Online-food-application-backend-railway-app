package com.unicksbyte.Online_food_ordering_app.controller;

import com.unicksbyte.Online_food_ordering_app.io.OrderResponse;
import com.unicksbyte.Online_food_ordering_app.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/admin/orders")
@AllArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;

    //API for admin panel
    @GetMapping("/all")
    public List<OrderResponse> getOrdersOfAllUsers() {
        return orderService.getOrdersOfAllUsers();
    }

    //API for admin panel update order status
    @PatchMapping("/status/{orderId}")
    public void updateOrderStatus(@PathVariable String orderId, @RequestParam String status) {
        orderService.updateOrderStatus(orderId, status);

    }
}
