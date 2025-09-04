package com.unicksbyte.Online_food_ordering_app.service;

import com.unicksbyte.Online_food_ordering_app.entity.UserEntity;
import com.unicksbyte.Online_food_ordering_app.io.UserRequest;
import com.unicksbyte.Online_food_ordering_app.io.UserResponse;
import com.unicksbyte.Online_food_ordering_app.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationFacade authenticationFacade;

    @Override
    public UserResponse registerUser(UserRequest request) {
        UserEntity newUser = convertToEntity(request);
        newUser = userRepository.save(newUser);
        return convertToUserResponse(newUser);

    }

    @Override
    public String findByUserId() {
        Authentication auth = authenticationFacade.getAuthentication();
        String loggedInUserEmail = auth.getName();

        UserEntity loggedInUser = userRepository.findByEmail(loggedInUserEmail).orElseThrow(() -> new UsernameNotFoundException("username not found..!"));
        return loggedInUser.getId();
    }

    private UserEntity convertToEntity(UserRequest request)
    {
      return UserEntity.builder()
              .email(request.getEmail())
              .password(passwordEncoder.encode(request.getPassword()))
              .name(request.getName())
              .role("USER")
              .build();
    }

    private UserResponse convertToUserResponse(UserEntity registeredUser)
    {
       return UserResponse.builder()
                .id(registeredUser.getId())
                .name(registeredUser.getName())
                .email(registeredUser.getEmail())
                .build();
    }
}
