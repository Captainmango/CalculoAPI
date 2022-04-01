package com.edward.calculoapi.api.dto.requests;

import com.fasterxml.jackson.databind.JsonNode;

import javax.validation.constraints.NotBlank;

public class TokenRefreshRequest {

    @NotBlank
    private String refreshToken;

    public TokenRefreshRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public TokenRefreshRequest(){}

    public String getRefreshToken() { return refreshToken; }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
