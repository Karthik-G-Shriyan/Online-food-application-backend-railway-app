package com.unicksbyte.Online_food_ordering_app.controller;


import com.unicksbyte.Online_food_ordering_app.io.AuthenticationRequest;
import com.unicksbyte.Online_food_ordering_app.io.AuthenticationResponse;
import com.unicksbyte.Online_food_ordering_app.service.AppUserDetailsService;
import com.unicksbyte.Online_food_ordering_app.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final AppUserDetailsService userDetailsService;

    private final JwtUtil jwtUtil;


    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody AuthenticationRequest request){

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
       final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

        boolean isUser = userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"));

        if (!isUser) {
            throw new RuntimeException("Access denied: not an valid user.");
        }

       final String jwtToken = jwtUtil.generateToken(userDetails);
       return new AuthenticationResponse(request.getEmail() ,jwtToken);
    }
}
