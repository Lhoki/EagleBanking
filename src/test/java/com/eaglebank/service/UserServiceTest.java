package com.eaglebank.service;

import com.eaglebank.domain.User;
import com.eaglebank.dto.request.UserCreateRequest;
import com.eaglebank.dto.response.UserResponse;
import com.eaglebank.exception.ConflictException;
import com.eaglebank.exception.ForbiddenException;
import com.eaglebank.exception.NotFoundException;
import com.eaglebank.mapper.UserMapper;
import com.eaglebank.repository.UserRepository;
import com.eaglebank.security.CustomUserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UserCreateRequest createRequest;
    private User user;
    private UserResponse userResponse;
    private UUID userId;
    private String userResponseId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        userResponseId = "usr-" + userId.toString().replace("-", "");
        createRequest = UserCreateRequest.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .password("password123")
                .build();

        user = User.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .passwordHash("hashed_password")
                .build();
        user.setId(userId);

        userResponse = UserResponse.builder()
                .id(userResponseId)
                .name("John Doe")
                .email("john.doe@example.com")
                .build();
    }

    @Test
    void createUser_Success() {
        when(userRepository.existsByEmail(createRequest.getEmail())).thenReturn(false);
        when(userMapper.toEntity(createRequest)).thenReturn(user);
        when(passwordEncoder.encode(createRequest.getPassword())).thenReturn("hashed_password");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse result = userService.createUser(createRequest);

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(createRequest.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_DuplicateEmail_ThrowsConflict() {
        when(userRepository.existsByEmail(createRequest.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser(createRequest))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    void getUser_Success() {
        setupSecurityContext(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse result = userService.getUser(userResponseId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(userResponseId);
    }

    @Test
    void getUser_NotFound_ThrowsNotFound() {
        setupSecurityContext(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUser(userResponseId))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void getUser_OtherUser_ThrowsForbidden() {
        setupSecurityContext(UUID.randomUUID());

        assertThatThrownBy(() -> userService.getUser(userResponseId))
                .isInstanceOf(ForbiddenException.class);
    }

    private void setupSecurityContext(UUID id) {
        CustomUserPrincipal principal = new CustomUserPrincipal(user);
        // Overriding the id since the one from the user might be different in other tests
        User userWithId = User.builder().email("john.doe@example.com").passwordHash("hash").build();
        userWithId.setId(id);
        principal = new CustomUserPrincipal(userWithId);

        Authentication auth = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
    }
}
