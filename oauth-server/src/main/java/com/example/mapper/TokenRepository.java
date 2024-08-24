package com.example.mapper;

import com.example.entity.TokenDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<TokenDO, String> {
    TokenDO findByTokenId(String tokenId);
}
