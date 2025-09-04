package com.unicksbyte.Online_food_ordering_app.io;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OrderItem {

    private String foodId;
    private String category;
    private String name;
    private String description;
    private String imageUrl;
    private int quantity;
    private double price;
}
