package com.edward.calculoapi.api.dto.responses;

import com.edward.calculoapi.models.RefreshToken;

import java.util.List;

public class LogInResponse {

    private Long id;
    private String firstName;
    private String email;
    private List<String> roles;
    private String refreshToken;

    public LogInResponse(Long id, String firstName, String email, List<String> roles, String refreshToken) {
        this.id = id;
        this.firstName = firstName;
        this.email = email;
        this.roles = roles;
        this.refreshToken = refreshToken;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
