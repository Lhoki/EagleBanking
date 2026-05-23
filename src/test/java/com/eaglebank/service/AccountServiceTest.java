package com.eaglebank.service;

import com.eaglebank.domain.AccountType;
import com.eaglebank.domain.BankAccount;
import com.eaglebank.domain.User;
import com.eaglebank.dto.request.AccountCreateRequest;
import com.eaglebank.dto.response.AccountResponse;
import com.eaglebank.exception.NotFoundException;
import com.eaglebank.mapper.AccountMapper;
import com.eaglebank.repository.AccountRepository;
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

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private AccountService accountService;

    private User user;
    private UUID userId;
    private AccountCreateRequest createRequest;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = User.builder()
                .email("test@example.com")
                .build();
        user.setId(userId);

        createRequest = AccountCreateRequest.builder()
                .name("My Account")
                .accountType(AccountType.personal)
                .build();
    }

    @Test
    void createAccount_Success() {
        setupSecurityContext(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(accountRepository.existsByAccountNumber(any())).thenReturn(false);
        when(accountRepository.save(any(BankAccount.class))).thenAnswer(i -> i.getArguments()[0]);
        when(accountMapper.toResponse(any(BankAccount.class))).thenReturn(AccountResponse.builder().name("My Account").balance(BigDecimal.ZERO).build());

        AccountResponse result = accountService.createAccount(createRequest);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("My Account");
        assertThat(result.getBalance()).isZero();
        verify(accountRepository).save(any(BankAccount.class));
    }

    @Test
    void createAccount_UserNotFound_ThrowsNotFound() {
        setupSecurityContext(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.createAccount(createRequest))
                .isInstanceOf(NotFoundException.class);
    }

    private void setupSecurityContext(UUID id) {
        User userWithId = User.builder().email("test@example.com").build();
        userWithId.setId(id);
        CustomUserPrincipal principal = new CustomUserPrincipal(userWithId);

        Authentication auth = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
    }
}
