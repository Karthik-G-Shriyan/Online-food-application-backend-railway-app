package com.unicksbyte.Online_food_ordering_app.controller;

import com.unicksbyte.Online_food_ordering_app.io.AuthenticationRequest;
import com.unicksbyte.Online_food_ordering_app.io.AuthenticationResponse;
import com.unicksbyte.Online_food_ordering_app.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/admin")
@RequiredArgsConstructor
public class AdminAuthController {

    private final JwtUtil jwtUtil;

   private final UserDetailsService userDetailsService;

   private final AuthenticationManager authenticationManager;



    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody AuthenticationRequest request) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            throw new RuntimeException("Access denied: not an admin user.");}
        final String jwtToken = jwtUtil.generateToken(userDetails);
        return new AuthenticationResponse(request.getEmail() ,jwtToken);

    }
}

