
package com.talentflow.api.config;

import java.io.IOException;
import java.time.LocalDateTime;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationEntryPoint
        implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String timestamp = LocalDateTime.now().toString();
        String path = request.getRequestURI();

        String json = """
            {
                "success": false,
                "message": "Authentication required",
                "timestamp": "%s",
                "path": "%s"
            }
            """.formatted(timestamp, path);

        response.getWriter().write(json);
    }
}
