package com.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")

public class UserDO {
    @Id
    @Column(name = "user_id")
    private String userId;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private String oauthLoginName;

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

    public String getOAuthLoginName() {
        return oauthLoginName;
    }

    public void setOAuthLoginName(String oauthLoginName) {
        this.oauthLoginName = oauthLoginName;
    }
}
