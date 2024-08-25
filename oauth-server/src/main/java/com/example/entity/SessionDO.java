package com.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "session")
public class SessionDO {
    @Id
    @Column(name = "session_id")
    private String sessionId;

    @Column
    private String code;

    @Column
    private String userId;

    @Column
    private Boolean codeState;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String tokenId) {
        this.sessionId = tokenId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getCodeState() {
        return codeState;
    }

    public void setCodeState(Boolean tokenState) {
        this.codeState = tokenState;
    }
}
