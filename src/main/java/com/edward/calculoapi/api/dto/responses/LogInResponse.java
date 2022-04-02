package com.edward.calculoapi.api.dto.responses;

public class LogInResponse {

    private Long id;
    private String firstName;
    private String email;
    private String refreshToken;
    private String jwt;

    public LogInResponse(Long id, String firstName, String email, String refreshToken, String jwt) {
        this.id = id;
        this.firstName = firstName;
        this.email = email;
        this.refreshToken = refreshToken;
        this.jwt = jwt;
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

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getJwt() {
        return jwt;
    }
}
