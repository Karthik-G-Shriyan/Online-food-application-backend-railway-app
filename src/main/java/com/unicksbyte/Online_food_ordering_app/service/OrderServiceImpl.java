package com.unicksbyte.Online_food_ordering_app.service;


import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.unicksbyte.Online_food_ordering_app.entity.OrderEntity;
import com.unicksbyte.Online_food_ordering_app.io.OrderRequest;
import com.unicksbyte.Online_food_ordering_app.io.OrderResponse;
import com.unicksbyte.Online_food_ordering_app.repository.CartRepository;
import com.unicksbyte.Online_food_ordering_app.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import netscape.javascript.JSObject;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.xml.catalog.Catalog;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final UserService userService;

    private final CartRepository cartRepository;

    @Value("${razorpay.key}")
    private String RAZORPAY_KEY;

    @Value("${razorpay.secret}")
    private String RAZORPAY_SECRET;

    private final OrderRepository orderRepository;

    @Override
    public OrderResponse createOrderWithPayment(OrderRequest request) throws RazorpayException {
        OrderEntity newOrder = convertToEntity(request);
        newOrder = orderRepository.save(newOrder);

        //creating razorpay payment gateway

        RazorpayClient razorpayClient = new RazorpayClient(RAZORPAY_KEY, RAZORPAY_SECRET);
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount",newOrder.getAmount());
        orderRequest.put("currency", "INR");
        orderRequest.put("payment_capture", 1);

        Order razorpayOrder = razorpayClient.orders.create(orderRequest);
        newOrder.setRazorpayOrderId(razorpayOrder.get("id"));

        String loggedInUserId = userService.findByUserId();
        newOrder.setUserId(loggedInUserId);
        newOrder = orderRepository.save(newOrder);
        return convertToResponse(newOrder);

    }

    @Override
    public void verifyPayment(Map<String, String> paymentData, String status) {
        String razorpayOrderId = paymentData.get("razorpay_order_id");
        OrderEntity existingOrder = orderRepository.findByRazorpayOrderId(razorpayOrderId)
                .orElseThrow(() -> new RuntimeException("order not found"));
        existingOrder.setPaymentStatus(status);
        existingOrder.setRazorpaySignature(paymentData.get("razorpay_signature"));
        existingOrder.setRazorpayPaymentId(paymentData.get("razorpay_payment_id"));
        orderRepository.save(existingOrder);
        if("paid".equalsIgnoreCase(status)){
            cartRepository.deleteByUserId(existingOrder.getUserId());
        }

    }

    @Override
    public List<OrderResponse> getUserOrders() {
        String loggedInUserId = userService.findByUserId();
        List<OrderEntity>  listOfOrders= orderRepository.findByUserId(loggedInUserId);
        return  listOfOrders.stream().map(entity -> convertToResponse(entity)).collect(Collectors.toList());

    }

    @Override
    public void removeOrder(String orderId) {
        orderRepository.deleteById(orderId);

    }

    @Override
    public List<OrderResponse> getOrdersOfAllUsers() {
        List<OrderEntity> listOfOrders = orderRepository.findAll();
       return listOfOrders.stream().map(entity -> convertToResponse(entity)).collect(Collectors.toList());
    }

    @Override
    public void updateOrderStatus(String orderId, String status) {
        OrderEntity entity = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("order not found"));
        entity.setOrderStatus(status);
        orderRepository.save(entity);
    }

    private OrderResponse convertToResponse(OrderEntity newOrder) {
       return OrderResponse.builder()
                .id(newOrder.getId())
                .amount(newOrder.getAmount())
                .userAddress(newOrder.getUserAddress())
                .userId(newOrder.getUserId())
                .razorpayOrderId(newOrder.getRazorpayOrderId())
                .paymentStatus(newOrder.getPaymentStatus())
                .orderStatus(newOrder.getOrderStatus())
               .email(newOrder.getEmail())
               .phoneNumber(newOrder.getPhoneNumber())
               .orderedItems(newOrder.getOrderedItems())
                .build();
    }

    private OrderEntity convertToEntity(OrderRequest request) {
       return OrderEntity.builder()
                .userAddress(request.getUserAddress())
                .amount(request.getAmount())
                .orderedItems(request.getOrderedItems())
               .email(request.getEmail())
               .phoneNumber(request.getPhoneNumber())
               .orderStatus(request.getOrderStatus())
                .build();
    }
}
