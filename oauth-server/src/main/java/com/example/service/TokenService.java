package com.example.service;

import com.example.entity.TokenDO;
import com.example.exception.TokenNotFoundException;
import com.example.mapper.TokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    @Autowired
    TokenRepository tokenRepository;

    @Transactional
    public void TokenExists(TokenDO tokenDO) {
        if (tokenDO == null) {
            throw new TokenNotFoundException("Token not found");
        }
    }

    public void saveToken(TokenDO tokenDO) {
        tokenRepository.save(tokenDO);
    }

    @Transactional
    public TokenDO getTokenByTokenId(String tokenId) {
        TokenDO tokenDO = tokenRepository.findByTokenId(tokenId);
        TokenExists(tokenDO);
        return tokenDO;
    }

}
