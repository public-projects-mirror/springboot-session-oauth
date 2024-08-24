package com.example.controller;

import com.example.manager.BaseAuthManager;
import com.example.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

@Controller
@RequestMapping("/oauth")
@CrossOrigin
public class OAuthController {
    private static final String Authorize_URL = "http://localhost:5173/server/home";
    private static final String TOKEN_URL = "http://localhost:8081/oauth/token";
    private static final String USER_URL = "http://localhost:8081/oauth/user";
    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json");

    @Autowired
    private BaseAuthManager authManager;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/authorize", method = RequestMethod.GET)
    public void authorize(HttpServletResponse httpServletResponse, @RequestParam(required = false) String sessionId)
            throws IOException {
        String url = Authorize_URL + "?redirect_uri=http://localhost:8080/oauth/redirect";

        if (sessionId != null && !sessionId.isEmpty()) {
            url += "&state=" + sessionId;
        }
        httpServletResponse.sendRedirect(url);
    }

    @RequestMapping(value = "/redirect", method = RequestMethod.GET)
    public void redirect(@RequestParam("code") String tokenId,
                         @RequestParam(required = false, value = "state") String state,
                         HttpServletResponse httpServletResponse) throws IOException {
        OkHttpClient client = new OkHttpClient();

        HashMap<String, String> bodyMap = new HashMap<>();
        bodyMap.put("tokenId", tokenId);

        String reqBodyJson = new Gson().toJson(bodyMap);
        RequestBody requestBody = RequestBody.create(reqBodyJson, JSON_MEDIA_TYPE);
        Request request = new Request.Builder()
                .url(TOKEN_URL)
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                handleError(httpServletResponse, "Failed to get access token");
                return;
            }

            // 解析 JSON 响应以提取 access token
            assert response.body() != null;
            String accessToken = response.body().string();

            handleUserInfo(accessToken, state, httpServletResponse, client);

        } catch (IOException e) {
            e.printStackTrace();
            handleError(httpServletResponse, "Internal server error");
        }
    }

    private void handleUserInfo(String accessToken, String state, HttpServletResponse httpServletResponse, OkHttpClient client) throws IOException {
        Request userInfoRequest = new Request.Builder()
                .url(USER_URL)
                .header("Authorization", accessToken)
                .build();

        try (Response userInfoResponse = client.newCall(userInfoRequest).execute()) {
            if (!userInfoResponse.isSuccessful()) {
                handleError(httpServletResponse, "Failed to get user info");
                return;
            }

            // 将用户数据添加到模型中
            assert userInfoResponse.body() != null;
            String userInfoResponseBody = userInfoResponse.body().string();
            String oauthLoginName = findTextFromBody(userInfoResponseBody);
            if (state == null) { // 没有用户，只是登录
                String userId = UUID.randomUUID().toString();
                authManager.saveSession(accessToken, userId);
                userService.saveOauthUser(userId, oauthLoginName, oauthLoginName);
                httpServletResponse.sendRedirect("http://localhost:5173/client/home?sessionId=" + accessToken);
            } else { //已有用户，配置第三方登录
                String userId = authManager.getUserId(state);
                userService.addOauthLoginName(userId, oauthLoginName);
                httpServletResponse.sendRedirect("http://localhost:5173/client/home?state=" + state);
            }

        } catch (IOException e) {
            e.printStackTrace();
            handleError(httpServletResponse, "Internal server error");
        }
    }

    private void handleError(HttpServletResponse httpServletResponse, String message) throws IOException {
        httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, message);
    }

    private String findTextFromBody(String body) {
        try {
            // 创建 ObjectMapper 实例
            ObjectMapper objectMapper = new ObjectMapper();
            // 解析 JSON 字符串
            JsonNode rootNode = objectMapper.readTree(body);
            // 获取 "name" 字段的值
            JsonNode nameNode = rootNode.path("data");
            if (!nameNode.isMissingNode()) {
                return nameNode.asText();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
