package com.unicksbyte.Online_food_ordering_app.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unicksbyte.Online_food_ordering_app.io.FoodRequest;
import com.unicksbyte.Online_food_ordering_app.io.FoodResponse;
import com.unicksbyte.Online_food_ordering_app.service.FoodService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api/admin/foods")
@AllArgsConstructor
public class AdminFoodController {

    private final FoodService foodService;



    @GetMapping
    public List<FoodResponse> readFoods(){
        return foodService.readFoods();

    }

    @PostMapping
    public FoodResponse addFood(@RequestPart("food") String foodString,
                                @RequestPart("file") MultipartFile file){

        ObjectMapper objectMapper = new ObjectMapper();
        FoodRequest request = null;
        try{
            request = objectMapper.readValue(foodString, FoodRequest.class);

        }
        catch(JsonProcessingException ex){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"invalid JSON format");

        }
        FoodResponse response = foodService.addFood(request, file);
        return response;

    }

    @GetMapping("/{id}")
    public FoodResponse readFood(@PathVariable String id){

        return foodService.readFood(id);
    }


    @PutMapping("/{id}")
    public FoodResponse updateFood(@PathVariable String id,
                                   @RequestPart("food") String foodString,
                                   @RequestPart(value = "file", required = false) MultipartFile file) {

        ObjectMapper objectMapper = new ObjectMapper();
        FoodRequest request;
        try {
            request = objectMapper.readValue(foodString, FoodRequest.class);
        } catch (JsonProcessingException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid JSON format");
        }

        return foodService.updateFood(id, request, file);
    }



    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFood(@PathVariable String id){

        foodService.deleteFood(id);

    }





}
