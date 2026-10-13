package com.talentflow.api.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.talentflow.api.config.UserPrincipal;
import com.talentflow.api.dto.NotificationDTO;
import com.talentflow.api.service.NotificationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(
            NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<NotificationDTO.Response> createNotification(
            @Valid @RequestBody NotificationDTO.CreateRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(notificationService.createNotification(request, principal));
    }

    @GetMapping
    public ResponseEntity<List<NotificationDTO.Response>> getMyNotifications(
            @AuthenticationPrincipal UserPrincipal principal) {

        return ResponseEntity.ok(
            notificationService.getMyNotifications(principal)
        );
    }

    @GetMapping("/unread")
    public ResponseEntity<List<NotificationDTO.Response>> getMyUnreadNotifications(
            @AuthenticationPrincipal UserPrincipal principal) {

        return ResponseEntity.ok(
            notificationService.getMyUnreadNotifications(principal)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationDTO.Response> getMyNotificationById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal principal) {

        return ResponseEntity.ok(
            notificationService.getMyNotificationById(id, principal)
        );
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<NotificationDTO.Response> markAsRead(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal principal) {

        return ResponseEntity.ok(
            notificationService.markAsRead(id, principal)
        );
    }

    @PatchMapping("/read-all")
    public ResponseEntity<Map<String, Integer>> markAllAsRead(
            @AuthenticationPrincipal UserPrincipal principal) {

        int updatedCount = notificationService.markAllAsRead(principal);

        return ResponseEntity.ok(
            Map.of("updatedCount", updatedCount)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMyNotification(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal principal) {

        notificationService.deleteMyNotification(id, principal);

        return ResponseEntity.noContent().build();
    }
}
