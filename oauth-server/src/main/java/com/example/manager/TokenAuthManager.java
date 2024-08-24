package com.example.manager;

import com.example.entity.TokenDO;
import com.example.service.TokenService;

import java.util.UUID;

public class TokenAuthManager implements BaseAuthManager {

    TokenService tokenService;

    public TokenAuthManager(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public String createToken(String userId) {
        String tokenId = UUID.randomUUID().toString();
        this.saveToken(tokenId, userId);
        return tokenId;
    }

    @Override
    public void saveToken(String tokenId, String userId) {
        TokenDO tokenDO = new TokenDO();
        tokenDO.setTokenId(tokenId);
        tokenDO.setUserId(userId);
        tokenDO.setTokenState(false);
        tokenService.saveToken(tokenDO);
    }

    @Override
    public void activateToken(String tokenId) {
        TokenDO tokenDO = tokenService.getTokenByTokenId(tokenId);
        tokenDO.setTokenState(true);
        tokenService.saveToken(tokenDO);
    }

    @Override
    public String getUserInfo(String tokenId) {
        TokenDO tokenDO = tokenService.getTokenByTokenId(tokenId);
        if (tokenDO.getTokenState()) {
            return tokenDO.getUserId();
        } else {
            return null;
        }
    }
}
