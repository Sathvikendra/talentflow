package com.talentflow.api.config;

public final class AuthRules {

    private AuthRules() {
    }

    public static final String[] PUBLIC_ENDPOINTS = {
            "/api/v1/auth/login",
            "/actuator/health"
    };

    public static final String[] ADMIN_ENDPOINTS = {

        };      

        public static final String[] REVIEWER_ENDPOINTS = {
        // reviewer-only endpoints
        };

        public static final String[] VIEWER_ENDPOINTS = {
        // viewer-only endpoints
        };
}