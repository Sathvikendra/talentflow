package com.talentflow.api.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.talentflow.api.config.UserPrincipal;
import com.talentflow.api.dto.NotificationDTO;
import com.talentflow.api.entity.Notification;
import com.talentflow.api.entity.User;
import com.talentflow.api.exception.AccessDeniedException;
import com.talentflow.api.exception.NotificationNotFoundException;
import com.talentflow.api.repository.NotificationRepository;
import com.talentflow.api.repository.UserRepository;

@Service 
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationService(
            NotificationRepository notificationRepository,
            UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public NotificationDTO.Response createNotification(
            NotificationDTO.CreateRequest request,
            UserPrincipal principal) {

        requireAdminOrReviewer(principal);

        User user = userRepository.findById(request.userId())
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "User not found with ID: " + request.userId()
            ));

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setType(request.type());
        notification.setMessage(request.message());
        notification.setReferenceType(request.referenceType());
        notification.setReferenceId(request.referenceId());
        notification.setRead(false);

        return toResponse(notificationRepository.save(notification));
    }

    @Transactional(readOnly = true)
    public List<NotificationDTO.Response> getMyNotifications(
            UserPrincipal principal) {

        Long userId = getAuthenticatedUserId(principal);

        return notificationRepository
            .findByUserIdOrderByCreatedAtDesc(userId)
            .stream()
            .map(this::toResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public NotificationDTO.Response getMyNotificationById(
            Long id, UserPrincipal principal) {

        Notification notification = findNotification(id);
        verifyOwner(notification, principal);

        return toResponse(notification);
    }

    @Transactional(readOnly = true)
    public List<NotificationDTO.Response> getMyUnreadNotifications(
            UserPrincipal principal) {

        Long userId = getAuthenticatedUserId(principal);

        return notificationRepository
            .findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId)
            .stream()
            .map(this::toResponse)
            .toList();
    }

    @Transactional
    public NotificationDTO.Response markAsRead(
            Long id, UserPrincipal principal) {

        Notification notification = findNotification(id);
        verifyOwner(notification, principal);

        if (!notification.isRead()) {
            notification.setRead(true);
            notification.setReadAt(LocalDateTime.now());
        }

        return toResponse(notificationRepository.save(notification));
    }

    @Transactional
    public int markAllAsRead(UserPrincipal principal) {

        Long userId = getAuthenticatedUserId(principal);

        List<Notification> unreadNotifications =
            notificationRepository
                .findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId);

        LocalDateTime now = LocalDateTime.now();

        unreadNotifications.forEach(notification -> {
            notification.setRead(true);
            notification.setReadAt(now);
        });

        notificationRepository.saveAll(unreadNotifications);

        return unreadNotifications.size();
    }

    @Transactional
    public void deleteMyNotification(
            Long id, UserPrincipal principal) {

        Notification notification = findNotification(id);
        verifyOwner(notification, principal);

        notificationRepository.delete(notification);
    }

    private Notification findNotification(Long id) {
        return notificationRepository.findById(id)
            .orElseThrow(() -> new NotificationNotFoundException(id));
    }

    private Long getAuthenticatedUserId(UserPrincipal principal) {
        if (principal == null || principal.getId() == null) {
            throw new AccessDeniedException(
                "Authentication is required"
            );
        }

        return principal.getId();
    }

    private void verifyOwner(
            Notification notification, UserPrincipal principal) {

        Long authenticatedUserId = getAuthenticatedUserId(principal);

        if (!notification.getUser().getId()
                .equals(authenticatedUserId)) {
            throw new AccessDeniedException(
                "You can only access your own notifications"
            );
        }
    }

    private void requireAdminOrReviewer(UserPrincipal principal) {
        if (principal == null
                || principal.getRole() == null
                || (!"ADMIN".equals(principal.getRole().name())
                && !"REVIEWER".equals(principal.getRole().name()))) {

            throw new AccessDeniedException(
                "Only ADMIN or REVIEWER can create notifications"
            );
        }
    }

    private NotificationDTO.Response toResponse(
            Notification notification) {

        return new NotificationDTO.Response(
            notification.getId(),
            notification.getUser().getId(),
            notification.getType(),
            notification.getMessage(),
            notification.getReferenceType(),
            notification.getReferenceId(),
            notification.isRead(),
            notification.getCreatedAt(),
            notification.getReadAt()
        );
    }
}
