package com.example.service;

import com.example.dto.UserDTO;
import com.example.entity.UserDO;
import com.example.exception.ForbiddenException;
import com.example.exception.UserAlreadyExistsException;
import com.example.exception.UserNotFoundException;
import com.example.mapper.UserRepository;
import com.example.model.UserRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Transactional
    public void authenticate(String username, String password) {
        UserDO userDO = userRepository.findByUsername(username);
        if (userDO == null) {
            throw new UserNotFoundException("User not found with username: " + username);
        }
        if (!userDO.getPassword().equals(password)) {
            throw new ForbiddenException("Wrong password with username: " + username);
        }
    }

    @Transactional
    public void checkUserExists(String username) {
        UserDO userDO = userRepository.findByUsername(username);
        if (userDO != null) {
            throw new UserAlreadyExistsException("User already exists with username: " + username);
        }
    }

    public UserDTO saveUser(UserRequest userRequest) {
        UserDO userDO = new UserDO();
        userDO.setUserId(UUID.randomUUID().toString());
        userDO.setUsername(userRequest.getUsername());
        userDO.setPassword(userRequest.getPassword());
        userRepository.save(userDO);
        return UserDTO.newFromDO().apply(userDO);
    }

    public UserDTO findUserByUsername(String username) {
        UserDO userDO = userRepository.findByUsername(username);
        return UserDTO.newFromDO().apply(userDO);
    }

    public UserDTO findUserByUserId(String userId) {
        UserDO userDO = userRepository.findByUserId(userId);
        return UserDTO.newFromDO().apply(userDO);
    }

}
