package com.example.manager;

import com.example.entity.SessionDO;
import com.example.service.SessionService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SessionAuthManager implements BaseAuthManager {

    SessionService sessionService;

    public SessionAuthManager(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public Map<String, String> createSession(String userId) {
        String sessionId = UUID.randomUUID().toString();
        String code = UUID.randomUUID().toString();
        saveSession(sessionId, code, userId);
        Map<String, String> map = new HashMap<>();
        map.put("sessionId", sessionId);
        map.put("code", code);
        return map;
    }

    @Override
    public void saveSession(String sessionId, String code, String userId) {
        SessionDO sessionDO = new SessionDO();
        sessionDO.setSessionId(sessionId);
        sessionDO.setCode(code);
        sessionDO.setUserId(userId);
        sessionDO.setCodeState(false);
        sessionService.saveSession(sessionDO);
    }

    @Override
    public String getUserId(String sessionId) {
        SessionDO sessionDO = sessionService.getSessionBySessionId(sessionId);
        return sessionDO.getUserId();
    }

    @Override
    public void deleteSessionBySessionId(String sessionId) {
        SessionDO sessionDO = sessionService.getSessionBySessionId(sessionId);
        sessionService.deleteSession(sessionDO);
    }

    @Override
    public void activateCode(String code) {
        SessionDO sessionDO = sessionService.getSessionByCode(code);
        sessionDO.setCodeState(true);
        sessionService.saveSession(sessionDO);
    }

    @Override
    public String getUserInfo(String code) {
        SessionDO sessionDO = sessionService.getSessionByCode(code);
        if (sessionDO != null && sessionDO.getCodeState()) {
            return sessionDO.getUserId();
        }
        return null;
    }

    @Override
    public String getCodeFromSessionId(String sessionId) {
        SessionDO sessionDO = sessionService.getSessionBySessionId(sessionId);
        if (sessionDO != null) {
            return sessionDO.getCode();
        }
        return null;
    }
}
