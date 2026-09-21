package com.talentflow.api.dto;

import com.talentflow.api.entity.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserDTO {

    public record CreateUserRequest(

        @NotBlank
        @Size(max = 50)
        String username,

        @NotBlank
        @Email
        @Size(max = 100)
        String email,

        @NotBlank
        @Size(min = 8, max = 100)
        String password,

        @NotBlank
        @Size(max = 50)
        String firstName,

        @NotBlank
        @Size(max = 50)
        String lastName,

        Role role
    ) {}    

    public record UpdateUserRequest(
            String username,
            String email,
            String password,
            String firstName,
            String lastName,
            Role role
    ) {}

    public record UserResponse(
            Long id,
            String username,
            String email,
            String firstName,
            String lastName,
            Role role,
            boolean active
    ) {}
}