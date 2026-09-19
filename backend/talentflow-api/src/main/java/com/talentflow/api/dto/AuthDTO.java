package com.talentflow.api.dto;

import com.talentflow.api.entity.Role;

public class AuthDTO {

    public record LoginRequest(
            String username,
            String password
    ) {
    }

    public record LoginResponse(
            String token,
            String username,
            Role role
    ) {

    }
}