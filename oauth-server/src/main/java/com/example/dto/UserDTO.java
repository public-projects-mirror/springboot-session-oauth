package com.example.dto;

import com.example.entity.UserDO;

import java.util.function.Function;

public class UserDTO {
    private String userId;
    private String username;
    private String password;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static Function<UserDO, UserDTO> newFromDO() {
        return userDO -> {
            UserDTO userDTO = new UserDTO();
            userDTO.userId = userDO.getUserId();
            userDTO.username = userDO.getUsername();
            userDTO.password = userDO.getPassword();
            return userDTO;
        };
    }

}
