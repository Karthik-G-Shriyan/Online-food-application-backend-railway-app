package com.unicksbyte.Online_food_ordering_app.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unicksbyte.Online_food_ordering_app.io.FoodRequest;
import com.unicksbyte.Online_food_ordering_app.io.FoodResponse;
import com.unicksbyte.Online_food_ordering_app.service.FoodService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api/foods")
@AllArgsConstructor
public class FoodController {



    private final FoodService foodService;


    @GetMapping
    public List<FoodResponse> readFoods(){
        return foodService.readFoods();

    }


    @GetMapping("/{id}")
    public FoodResponse readFood(@PathVariable String id){

        return foodService.readFood(id);
    }



}
