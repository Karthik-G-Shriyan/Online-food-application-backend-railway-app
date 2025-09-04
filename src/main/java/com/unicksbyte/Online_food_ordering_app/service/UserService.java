package com.unicksbyte.Online_food_ordering_app.service;

import com.unicksbyte.Online_food_ordering_app.io.UserRequest;
import com.unicksbyte.Online_food_ordering_app.io.UserResponse;

public interface UserService {

    UserResponse registerUser(UserRequest request);

    String findByUserId();
}
