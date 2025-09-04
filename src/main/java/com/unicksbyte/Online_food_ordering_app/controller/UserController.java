package com.unicksbyte.Online_food_ordering_app.controller;


import com.unicksbyte.Online_food_ordering_app.io.UserRequest;
import com.unicksbyte.Online_food_ordering_app.io.UserResponse;
import com.unicksbyte.Online_food_ordering_app.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;



    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@RequestBody UserRequest request) {

        return userService.registerUser(request);

    }


}
