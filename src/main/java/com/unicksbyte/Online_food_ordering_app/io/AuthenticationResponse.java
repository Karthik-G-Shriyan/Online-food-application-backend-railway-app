package com.unicksbyte.Online_food_ordering_app.io;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthenticationResponse {

    private String email;
    private String token;
}
