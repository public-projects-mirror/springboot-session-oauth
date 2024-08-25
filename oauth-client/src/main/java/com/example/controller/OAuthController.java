package com.example.controller;

import com.example.service.OAuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
@RequestMapping("/oauth")
@CrossOrigin
public class OAuthController {
    private static final String Authorize_URL = "http://localhost:5173/server/home";

    @Autowired
    private OAuthService oAuthService;

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
    public void redirect(@RequestParam("code") String code,
                         @RequestParam(required = false, value = "state") String state,
                         HttpServletResponse httpServletResponse) throws Throwable {
        String accessCode = oAuthService.activateCode(code);
        String sessionId = oAuthService.processUserInfo(accessCode, state);
        String redirectUrl = (state == null) ?
                "http://localhost:5173/client/home?sessionId=" + sessionId :
                "http://localhost:5173/client/home?state=" + state;
        httpServletResponse.sendRedirect(redirectUrl);
    }
}
