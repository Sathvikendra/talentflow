
package com.talentflow.api.config;

import java.io.IOException;
import java.time.LocalDateTime;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class SecurityExceptionHandling implements AccessDeniedHandler {

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException exception
    ) throws IOException {

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String json = """
            {
                "success": false,
                "message": "You do not have permission to perform this action",
                "timestamp": "%s",
                "path": "%s"
            }
            """.formatted(
                LocalDateTime.now(),
                request.getRequestURI()
            );

        response.getWriter().write(json);
    }
}
