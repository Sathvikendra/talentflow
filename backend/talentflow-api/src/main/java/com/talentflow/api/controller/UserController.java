package com.talentflow.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.talentflow.api.dto.UserDTO;
import com.talentflow.api.entity.User;
import com.talentflow.api.service.UserService;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Create a new user.
     *
     * POST /api/v1/users
     *
     * Response: 201 CREATED
     */
    @PostMapping
    public ResponseEntity<UserDTO.UserResponse> createUser(@Valid 
            @RequestBody UserDTO.CreateUserRequest request
    ) {

        User user = userService.createUser(
                request.username(),
                request.email(),
                request.password(),
                request.firstName(),
                request.lastName(),
                request.role()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(toResponse(user));
    }

    @GetMapping("/by-username/{username}")
    public ResponseEntity<UserDTO.UserResponse> getUserByUsername(
            @PathVariable String username
    ) {

        User user = userService.getUserByUsername(username);

        return ResponseEntity.ok(toResponse(user));
    }

    /**
     * Get a user by ID.
     *
     * GET /api/v1/users/{id}
     *
     * Response: 200 OK
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO.UserResponse> getUserById(
            @PathVariable Long id
    ) {

        User user = userService.getUserById(id);

        return ResponseEntity.ok(toResponse(user));
    }

    /**
     * Get all users.
     *
     * GET /api/v1/users
     *
     * Response: 200 OK
     */
    @GetMapping
    public ResponseEntity<List<UserDTO.UserResponse>> getAllUsers() {

        List<UserDTO.UserResponse> users =
                userService.getAllUsers()
                        .stream()
                        .map(this::toResponse)
                        .toList();

        return ResponseEntity.ok(users);
    }

    /**
     * Update an existing user.
     *
     * PUT /api/v1/users/{id}
     *
     * Response: 200 OK
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO.UserResponse> updateUser(
            @PathVariable Long id,
            @RequestBody UserDTO.UpdateUserRequest request
    ) {

        User user = userService.updateUser(
                id,
                request.username(),
                request.email(),
                request.password(),
                request.firstName(),
                request.lastName(),
                request.role()
        );

        return ResponseEntity.ok(toResponse(user));
    }

    /**
     * Deactivate a user.
     *
     * PATCH /api/v1/users/{id}/deactivate
     *
     * Response: 200 OK
     */
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<UserDTO.UserResponse> deactivateUser(
            @PathVariable Long id
    ) {

        User user = userService.deactivateUser(id);

        return ResponseEntity.ok(toResponse(user));
    }

    /**
     * Converts the User entity into a safe API response.
     *
     * passwordHash is intentionally excluded.
     */
    private UserDTO.UserResponse toResponse(User user) {

        return new UserDTO.UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole(),
                user.isActive()
        );
    }
}