package com.example.manager;

import java.util.Map;

public interface BaseAuthManager {
    void saveSession(String sessionId, String code, String userId);
    Map<String, String> createSession(String userId);
    String getUserId(String sessionId);
    void deleteSessionBySessionId(String sessionId);
    void activateCode(String code);
    String getUserInfo(String code);
    String getCodeFromSessionId(String sessionId);
}
