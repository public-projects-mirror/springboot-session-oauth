package com.example.manager;

public interface BaseAuthManager {
    void saveToken(String tokenId, String userId);
    String createToken(String userId);
    void activateToken(String tokenId);
    String getUserInfo(String tokenId);
}
