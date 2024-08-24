package com.example.controller;

import com.example.dto.UserDTO;
import com.example.model.UserRequest;
import com.example.response.ApiResponse;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ApiResponse<String> registerUser(@RequestBody UserRequest userRequest) {
        String username = userRequest.getUsername();
        userService.checkUserExists(username);
        UserDTO userDTO = userService.saveUser(userRequest);
        String userId = userDTO.getUserId();
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setStatus(HttpStatus.OK);
        apiResponse.setMessage("User registered successfully");
        apiResponse.setData(userId);
        return apiResponse;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ApiResponse<String> loginUser(@RequestBody UserRequest userRequest) {
        String username = userRequest.getUsername();
        String password = userRequest.getPassword();
        userService.authenticate(username, password);
        UserDTO userDTO = userService.findUserByUsername(username);
        String userId = userDTO.getUserId();
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setStatus(HttpStatus.OK);
        apiResponse.setMessage("Successfully logged in");
        apiResponse.setData(userId);
        return apiResponse;
    }
}
