package com.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "token")
public class TokenDO {
    @Id
    @Column(name = "token_id")
    private String tokenId;

    @Column
    private String UserId;

    @Column
    private Boolean tokenState;

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public Boolean getTokenState() {
        return tokenState;
    }

    public void setTokenState(Boolean tokenState) {
        this.tokenState = tokenState;
    }
}
