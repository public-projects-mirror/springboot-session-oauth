package com.example.service;

import com.example.exception.OAuth2TokenRetrievalException;
import com.example.exception.UserInfoProcessingException;
import com.example.manager.BaseAuthManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;


@Service
public class OAuthService {
    private static final Logger logger = LoggerFactory.getLogger(OAuthService.class);
    private static final String ACTIVATE_URL = "http://localhost:8081/oauth/activate";
    private static final String USER_URL = "http://localhost:8081/oauth/getUserInfo";
    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json");

    @Autowired
    private BaseAuthManager authManager;

    @Autowired
    private UserService userService;

    @Transactional
    public String activateCode(String code) {
        OkHttpClient client = new OkHttpClient();

        HashMap<String, String> bodyMap = new HashMap<>();
        bodyMap.put("code", code);

        String reqBodyJson = new Gson().toJson(bodyMap);
        RequestBody requestBody = RequestBody.create(reqBodyJson, JSON_MEDIA_TYPE);
        Request request = new Request.Builder()
                .url(ACTIVATE_URL)
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new OAuth2TokenRetrievalException("Failed to get access token");
            }
            assert response.body() != null;
            return response.body().string();
        } catch (IOException e) {
            logger.error("Error during OAuth2 token retrieval", e);
            throw new OAuth2TokenRetrievalException("Error during OAuth2 token retrieval", e);
        }
    }

    @Transactional
    public String processUserInfo(String accessToken, String state) {
        OkHttpClient client = new OkHttpClient();
        Request userInfoRequest = new Request.Builder()
                .url(USER_URL)
                .header("Authorization", accessToken)
                .build();
        try (Response userInfoResponse = client.newCall(userInfoRequest).execute()) {
            if (!userInfoResponse.isSuccessful()) {
                throw new UserInfoProcessingException("Failed to get user info");
            }

            // 将用户数据添加到模型中
            assert userInfoResponse.body() != null;
            String userInfoResponseBody = userInfoResponse.body().string();
            String oauthLoginName = findTextFromBody(userInfoResponseBody);

            if (state == null) { // 没有用户，只是登录
                String userId = UUID.randomUUID().toString();
                authManager.saveSession(accessToken, userId);
                userService.saveOAuthUser(userId, oauthLoginName, oauthLoginName);
                return accessToken;
            } else { //已有用户，配置第三方登录
                String userId = authManager.getUserId(state);
                userService.addOAuthLoginName(userId, oauthLoginName);
                return null;
            }
        } catch (IOException e) {
            logger.error("Error during OAuth2 token retrieval", e);
            throw new UserInfoProcessingException("Error during user info handling", e);
        }
    }

    private String findTextFromBody(String body) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(body);
            JsonNode nameNode = rootNode.path("data");
            return !nameNode.isMissingNode() ? nameNode.asText() : null;
        } catch (Exception e) {
            logger.error("Error parsing JSON response", e);
            return null;
        }
    }

}
