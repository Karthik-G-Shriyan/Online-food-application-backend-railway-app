package com.unicksbyte.Online_food_ordering_app.service;

import com.unicksbyte.Online_food_ordering_app.io.FoodRequest;
import com.unicksbyte.Online_food_ordering_app.io.FoodResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FoodService {

    FoodResponse updateFood(String id, FoodRequest request, MultipartFile file);

    String uploadFile(MultipartFile file);

    FoodResponse addFood(FoodRequest request, MultipartFile file);

    List<FoodResponse> readFoods();

    FoodResponse readFood(String id);

    boolean deleteFile(String file);

    void deleteFood(String id);
}
