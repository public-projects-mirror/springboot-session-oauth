package com.example.controller;

import com.example.dto.UserDTO;
import com.example.manager.BaseAuthManager;
import com.example.model.ActivateTokenRequest;
import com.example.response.ApiResponse;
import com.example.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/oauth")
@CrossOrigin
public class OAuthController {
    private final UserService userService;
    private final BaseAuthManager authManager;

    @Autowired
    public OAuthController(UserService userService, BaseAuthManager authManager) {
        this.userService = userService;
        this.authManager = authManager;
    }

    @RequestMapping(value = "/authorize", method = RequestMethod.GET)
    public void authorize(@RequestParam("redirect_uri") String redirectUri,
                            @RequestParam("user_id") String userId,
                            @RequestParam(value = "state", required = false) String state,
                            HttpServletResponse httpServletResponse) throws IOException {
        String tokenId = authManager.createToken(userId);
        redirectUri = redirectUri + "?code=" + tokenId;
        if (state != null && !state.isEmpty()) {
            redirectUri += "&state=" + state;
        }
        httpServletResponse.sendRedirect(redirectUri);
    }

    @RequestMapping(value = "/token", method = RequestMethod.POST)
    public String activate(@RequestBody ActivateTokenRequest activateTokenRequest) {
        String tokenId = activateTokenRequest.getTokenId();
        authManager.activateToken(tokenId);
        return tokenId;
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public ApiResponse<String> getUserInfo(@RequestHeader ("Authorization") String tokenId) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        String userId = authManager.getUserInfo(tokenId);
        UserDTO userDTO = userService.findUserByUserId(userId);
        String username = userDTO.getUsername();
        apiResponse.setData(username);
        apiResponse.setMessage("get user info successfully");
        apiResponse.setStatus(HttpStatus.OK);
        return apiResponse;
    }
}
