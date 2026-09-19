package com.talentflow.api.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.talentflow.api.config.JwtUtils;
import com.talentflow.api.config.UserPrincipal;
import com.talentflow.api.dto.AuthDTO;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthService(
            AuthenticationManager authenticationManager,
            JwtUtils jwtUtils
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    public AuthDTO.LoginResponse login(
            AuthDTO.LoginRequest request
    ) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.username(),
                                request.password()
                        )
                );

        UserPrincipal userPrincipal =
                (UserPrincipal) authentication.getPrincipal();

        String token =
                jwtUtils.generateToken(userPrincipal);

        // String role =
        //         userPrincipal.getAuthorities()
        //                 .iterator()
        //                 .next()
        //                 .getAuthority()
        //                 .replace("ROLE_", "");

        return new AuthDTO.LoginResponse(
                token,
                userPrincipal.getUsername(),
                userPrincipal.getRole()
        );
    }
}