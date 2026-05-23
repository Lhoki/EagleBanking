package com.eaglebank.service;

import com.eaglebank.domain.AccountType;
import com.eaglebank.domain.BankAccount;
import com.eaglebank.domain.Transaction;
import com.eaglebank.domain.TransactionType;
import com.eaglebank.domain.User;
import com.eaglebank.dto.request.TransactionCreateRequest;
import com.eaglebank.dto.response.TransactionResponse;
import com.eaglebank.exception.ValidationException;
import com.eaglebank.mapper.TransactionMapper;
import com.eaglebank.repository.AccountRepository;
import com.eaglebank.repository.TransactionRepository;
import com.eaglebank.security.CustomUserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private TransactionService transactionService;

    private BankAccount account;
    private User user;

    @BeforeEach
    void setUp() {
        UUID userId = UUID.randomUUID();
        user = User.builder()
                .email("test@example.com")
                .build();
        user.setId(userId);

        account = BankAccount.builder()
                .user(user)
                .accountNumber("01234567")
                .accountType(AccountType.personal)
                .balance(BigDecimal.valueOf(100))
                .build();
        account.setId(UUID.randomUUID());

        setupSecurityContext(userId);
    }

    @Test
    void createTransaction_GeneratesOpenApiCompliantTransactionId() {
        TransactionCreateRequest request = TransactionCreateRequest.builder()
                .type("deposit")
                .amount(BigDecimal.TEN)
                .reference("Pay in")
                .build();

        when(accountRepository.findByAccountNumber("01234567")).thenReturn(Optional.of(account));
        when(transactionRepository.findByAccount(account)).thenReturn(List.of());
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(transactionMapper.toResponse(any(Transaction.class))).thenAnswer(invocation -> {
            Transaction transaction = invocation.getArgument(0);
            return TransactionResponse.builder()
                    .id(transaction.getTransactionId())
                    .build();
        });

        TransactionResponse result = transactionService.createTransaction("01234567", request);

        assertThat(result.getId()).isEqualTo("tan-A");
        assertThat(result.getId()).matches("^tan-[A-Za-z0-9]$");

        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(transactionCaptor.capture());
        assertThat(transactionCaptor.getValue().getTransactionId()).isEqualTo("tan-A");
    }

    @Test
    void fetchTransactionById_UsesOpenApiTransactionId() {
        Transaction transaction = Transaction.builder()
                .account(account)
                .transactionId("tan-A")
                .amount(BigDecimal.TEN)
                .type(TransactionType.deposit)
                .build();

        when(accountRepository.findByAccountNumber("01234567")).thenReturn(Optional.of(account));
        when(transactionRepository.findByAccountAndTransactionId(account, "tan-A")).thenReturn(Optional.of(transaction));
        when(transactionMapper.toResponse(transaction)).thenReturn(TransactionResponse.builder().id("tan-A").build());

        TransactionResponse result = transactionService.fetchTransactionById("01234567", "tan-A");

        assertThat(result.getId()).isEqualTo("tan-A");
        verify(transactionRepository).findByAccountAndTransactionId(account, "tan-A");
    }

    @Test
    void fetchTransactionById_InvalidFormat_ThrowsValidationException() {
        when(accountRepository.findByAccountNumber("01234567")).thenReturn(Optional.of(account));

        assertThatThrownBy(() -> transactionService.fetchTransactionById("01234567", "tan-dbSNfXjkvLdECtwAAMmsnUfALib4pk8wlwpGTJnaPyPrNhI9mTfoUzXXEyhYo76rAuchfekqxy6dVAMtdoKohK37DAVxOBvTZKV"))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Invalid transaction ID format");
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
