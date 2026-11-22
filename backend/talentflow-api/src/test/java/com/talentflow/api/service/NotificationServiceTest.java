
package com.talentflow.api.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.talentflow.api.config.UserPrincipal;
import com.talentflow.api.dto.NotificationDTO;
import com.talentflow.api.entity.Notification;
import com.talentflow.api.entity.Role;
import com.talentflow.api.entity.User;
import com.talentflow.api.exception.NotificationNotFoundException;
import com.talentflow.api.repository.NotificationRepository;
import com.talentflow.api.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private NotificationService notificationService;

    private UserPrincipal principal;
    private Notification notification;

    @BeforeEach
    void setUp() {

        principal = new UserPrincipal(
                1L,
                "admin",
                "password",
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN")),
                true,
                Role.ADMIN
        );

        User owner = mock(User.class);

        notification = new Notification();
        notification.setUser(owner);
        notification.setType("SYSTEM");
        notification.setMessage("Test notification");
    }

    // --------------------------------------------------
    // TEST HELPER
    // --------------------------------------------------

    private Notification createNotificationForUser(Long userId) {

        User user = mock(User.class);
        when(user.getId()).thenReturn(userId);

        Notification newNotification = new Notification();
        newNotification.setUser(user);
        newNotification.setType("SYSTEM");
        newNotification.setMessage("Test notification");

        return newNotification;
    }

    // --------------------------------------------------
    // GET NOTIFICATION BY ID
    // --------------------------------------------------

    @Test
    void getMyNotificationById_shouldReturnNotificationWhenUserIsOwner() {

        when(notification.getUser().getId()).thenReturn(1L);

        when(notificationRepository.findById(1L))
                .thenReturn(Optional.of(notification));

        var response =
                notificationService.getMyNotificationById(1L, principal);

        assertNotNull(response);
        assertEquals(1L, response.userId());
        assertEquals("SYSTEM", response.type());
        assertEquals("Test notification", response.message());

        verify(notificationRepository).findById(1L);
    }

    @Test
    void getMyNotificationById_shouldThrowAccessDeniedWhenUserIsNotOwner() {

        User anotherOwner = mock(User.class);
        when(anotherOwner.getId()).thenReturn(2L);

        notification.setUser(anotherOwner);

        when(notificationRepository.findById(1L))
                .thenReturn(Optional.of(notification));

        assertThrows(
                AccessDeniedException.class,
                () -> notificationService.getMyNotificationById(
                        1L, principal)
        );
    }

    @Test
    void getMyNotificationById_shouldThrowWhenNotificationDoesNotExist() {

        when(notificationRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                NotificationNotFoundException.class,
                () -> notificationService.getMyNotificationById(
                        999L, principal)
        );
    }

    // --------------------------------------------------
    // GET MY NOTIFICATIONS
    // --------------------------------------------------

    @Test
    void getMyNotifications_shouldReturnCurrentUsersNotifications() {

        Notification first = createNotificationForUser(1L);
        Notification second = createNotificationForUser(1L);

        when(notificationRepository.findByUserIdOrderByCreatedAtDesc(1L))
                .thenReturn(List.of(first, second));

        var response = notificationService.getMyNotifications(principal);

        assertEquals(2, response.size());
        assertEquals(1L, response.get(0).userId());
        assertEquals("SYSTEM", response.get(0).type());

        verify(notificationRepository)
                .findByUserIdOrderByCreatedAtDesc(1L);
    }

    // --------------------------------------------------
    // GET UNREAD NOTIFICATIONS
    // --------------------------------------------------

    @Test
    void getMyUnreadNotifications_shouldReturnOnlyUnreadNotifications() {

        Notification unread = createNotificationForUser(1L);

        when(notificationRepository
                .findByUserIdAndIsReadFalseOrderByCreatedAtDesc(1L))
                .thenReturn(List.of(unread));

        var response =
                notificationService.getMyUnreadNotifications(principal);

        assertEquals(1, response.size());
        assertFalse(response.get(0).isRead());

        verify(notificationRepository)
                .findByUserIdAndIsReadFalseOrderByCreatedAtDesc(1L);
    }

    // --------------------------------------------------
    // CREATE NOTIFICATION
    // --------------------------------------------------

    @Test
    void createNotification_shouldAllowAdminToCreateNotification() {

        User targetUser = mock(User.class);
        when(targetUser.getId()).thenReturn(2L);

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(targetUser));

        when(notificationRepository.save(any(Notification.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        NotificationDTO.CreateRequest request =
                new NotificationDTO.CreateRequest(
                        2L,
                        "SYSTEM",
                        "Requirement updated",
                        "REQUIREMENT",
                        10L
                );

        var response =
                notificationService.createNotification(request, principal);

        assertNotNull(response);
        assertEquals(2L, response.userId());
        assertEquals("SYSTEM", response.type());
        assertEquals("Requirement updated", response.message());
        assertEquals("REQUIREMENT", response.referenceType());
        assertEquals(10L, response.referenceId());
        assertFalse(response.isRead());

        verify(userRepository).findById(2L);
        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    void createNotification_shouldAllowReviewerToCreateNotification() {

        UserPrincipal reviewer = new UserPrincipal(
                3L,
                "reviewer",
                "password",
                List.of(new SimpleGrantedAuthority("ROLE_REVIEWER")),
                true,
                Role.REVIEWER
        );

        User targetUser = mock(User.class);
        when(targetUser.getId()).thenReturn(2L);

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(targetUser));

        when(notificationRepository.save(any(Notification.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        NotificationDTO.CreateRequest request =
                new NotificationDTO.CreateRequest(
                        2L,
                        "SYSTEM",
                        "Test message",
                        null,
                        null
                );

        var response =
                notificationService.createNotification(request, reviewer);

        assertNotNull(response);
        assertEquals(2L, response.userId());
        assertEquals("SYSTEM", response.type());

        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    void createNotification_shouldRejectViewer() {

        UserPrincipal viewer = new UserPrincipal(
                4L,
                "viewer",
                "password",
                List.of(new SimpleGrantedAuthority("ROLE_VIEWER")),
                true,
                Role.VIEWER
        );

        NotificationDTO.CreateRequest request =
                new NotificationDTO.CreateRequest(
                        2L,
                        "SYSTEM",
                        "Test message",
                        null,
                        null
                );

        assertThrows(
                AccessDeniedException.class,
                () -> notificationService.createNotification(request, viewer)
        );

        verify(userRepository, never()).findById(anyLong());
        verify(notificationRepository, never())
                .save(any(Notification.class));
    }

    @Test
    void createNotification_shouldReturnNotFoundWhenTargetUserDoesNotExist() {

        when(userRepository.findById(999L))
                .thenReturn(Optional.empty());

        NotificationDTO.CreateRequest request =
                new NotificationDTO.CreateRequest(
                        999L,
                        "SYSTEM",
                        "Test message",
                        null,
                        null
                );

        assertThrows(
                ResponseStatusException.class,
                () -> notificationService.createNotification(request, principal)
        );

        verify(userRepository).findById(999L);
        verify(notificationRepository, never())
                .save(any(Notification.class));
    }

    // --------------------------------------------------
    // MARK AS READ
    // --------------------------------------------------

    @Test
    void markAsRead_shouldMarkNotificationAsRead() {

        Notification unread = createNotificationForUser(1L);

        when(notificationRepository.findById(1L))
                .thenReturn(Optional.of(unread));

        when(notificationRepository.save(any(Notification.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var response = notificationService.markAsRead(1L, principal);

        assertTrue(response.isRead());
        assertNotNull(response.readAt());

        verify(notificationRepository).save(unread);
    }

    @Test
    void markAsRead_shouldPreserveReadAtWhenAlreadyRead() {

        Notification alreadyRead = createNotificationForUser(1L);

        LocalDateTime originalReadAt =
                LocalDateTime.of(2026, 11, 20, 10, 30);

        alreadyRead.setRead(true);
        alreadyRead.setReadAt(originalReadAt);

        when(notificationRepository.findById(1L))
                .thenReturn(Optional.of(alreadyRead));

        when(notificationRepository.save(any(Notification.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var response = notificationService.markAsRead(1L, principal);

        assertTrue(response.isRead());
        assertEquals(originalReadAt, response.readAt());

        verify(notificationRepository).save(alreadyRead);
    }

    @Test
    void markAsRead_shouldNotModifyNotificationWhenUserIsNotOwner() {

        User anotherOwner = mock(User.class);
        when(anotherOwner.getId()).thenReturn(2L);

        notification.setUser(anotherOwner);

        when(notificationRepository.findById(1L))
                .thenReturn(Optional.of(notification));

        assertThrows(
                AccessDeniedException.class,
                () -> notificationService.markAsRead(1L, principal)
        );

        verify(notificationRepository, never())
                .save(any(Notification.class));
    }

    // --------------------------------------------------
    // MARK ALL AS READ
    // --------------------------------------------------

    
    @Test
    void markAllAsRead_shouldMarkAllUnreadNotifications() {

        User firstUser = mock(User.class);
        User secondUser = mock(User.class);

        Notification first = new Notification();
        first.setUser(firstUser);
        first.setType("SYSTEM");
        first.setMessage("First notification");

        Notification second = new Notification();
        second.setUser(secondUser);
        second.setType("SYSTEM");
        second.setMessage("Second notification");

        when(notificationRepository
                .findByUserIdAndIsReadFalseOrderByCreatedAtDesc(1L))
                .thenReturn(new ArrayList<>(List.of(first, second)));

        when(notificationRepository.saveAll(anyList()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        int updatedCount = notificationService.markAllAsRead(principal);

        assertEquals(2, updatedCount);

        assertTrue(first.isRead());
        assertTrue(second.isRead());

        assertNotNull(first.getReadAt());
        assertNotNull(second.getReadAt());

        assertEquals(first.getReadAt(), second.getReadAt());

        verify(notificationRepository).saveAll(anyList());
    }

    @Test
    void markAllAsRead_shouldReturnZeroWhenNoUnreadNotificationsExist() {

        when(notificationRepository
                .findByUserIdAndIsReadFalseOrderByCreatedAtDesc(1L))
                .thenReturn(List.of());

        when(notificationRepository.saveAll(anyList()))
                .thenReturn(List.of());

        int updatedCount = notificationService.markAllAsRead(principal);

        assertEquals(0, updatedCount);

        verify(notificationRepository).saveAll(anyList());
    }

    // --------------------------------------------------
    // DELETE NOTIFICATION
    // --------------------------------------------------

    @Test
    void deleteMyNotification_shouldDeleteNotificationWhenUserIsOwner() {

        Notification ownedNotification = createNotificationForUser(1L);

        when(notificationRepository.findById(1L))
                .thenReturn(Optional.of(ownedNotification));

        notificationService.deleteMyNotification(1L, principal);

        verify(notificationRepository).delete(ownedNotification);
    }

    @Test
    void deleteMyNotification_shouldRejectDeletionByAnotherUser() {

        Notification anotherUsersNotification =
                createNotificationForUser(2L);

        when(notificationRepository.findById(1L))
                .thenReturn(Optional.of(anotherUsersNotification));

        assertThrows(
                AccessDeniedException.class,
                () -> notificationService.deleteMyNotification(
                        1L, principal)
        );

        verify(notificationRepository, never())
                .delete(any(Notification.class));
    }
}
