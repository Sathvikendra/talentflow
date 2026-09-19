package com.talentflow.api.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.talentflow.api.entity.Role;
import com.talentflow.api.entity.User;
import com.talentflow.api.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Creates a new user.
     *
     * The password received by this method is plain text and is
     * immediately converted to a BCrypt hash before being stored.
     */
    public User createUser(
            String username,
            String email,
            String password,
            String firstName,
            String lastName,
            Role role
    ) {

        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException(
                    "Username already exists"
            );
        }

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(
                    "Email already exists"
            );
        }

        User user = new User();

        user.setUsername(username);
        user.setEmail(email);

        // Never store the plain-text password.
        user.setPasswordHash(
                passwordEncoder.encode(password)
        );

        user.setFirstName(firstName);
        user.setLastName(lastName);

        // If no role is supplied, default to VIEWER.
        user.setRole(
                role != null ? role : Role.VIEWER
        );

        user.setActive(true);
        user.setLastLoginAt(null);

        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            throw new IllegalArgumentException(
                    "Username or email already exists"
            );
        }
    }

    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {

        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "User not found with username: " + username
                        )
                );
    }

    /**
     * Retrieves a user by ID.
     */
    @Transactional(readOnly = true)
    public User getUserById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "User not found with id: " + id
                        )
                );
    }

    /**
     * Retrieves all users.
     */
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {

        return userRepository.findAll();
    }

    /**
     * Updates an existing user.
     *
     * Password is updated only when a new password is supplied.
     */
    public User updateUser(
            Long id,
            String username,
            String email,
            String password,
            String firstName,
            String lastName,
            Role role
    ) {

        User user = getUserById(id);

        /*
         * Check username uniqueness only when the username
         * is actually being changed.
         */
        if (username != null &&
                !username.equals(user.getUsername())) {

            if (userRepository.existsByUsername(username)) {
                throw new IllegalArgumentException(
                        "Username already exists"
                );
            }

            user.setUsername(username);
        }

        /*
         * Check email uniqueness only when the email
         * is actually being changed.
         */
        if (email != null &&
                !email.equals(user.getEmail())) {

            if (userRepository.existsByEmail(email)) {
                throw new IllegalArgumentException(
                        "Email already exists"
                );
            }

            user.setEmail(email);
        }

        if (password != null && !password.isBlank()) {
            user.setPasswordHash(
                    passwordEncoder.encode(password)
            );
        }

        if (firstName != null) {
            user.setFirstName(firstName);
        }

        if (lastName != null) {
            user.setLastName(lastName);
        }

        if (role != null) {
            user.setRole(role);
        }

        user.setUpdatedAt(LocalDateTime.now());

        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            throw new IllegalArgumentException(
                    "Username or email already exists"
            );
        }
    }

    /**
     * Deactivates a user.
     *
     * The user is not physically deleted from the database.
     */
    public User deactivateUser(Long id) {

        User user = getUserById(id);

        if (!user.isActive()) {
            return user;
        }

        user.setActive(false);
        user.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }
}