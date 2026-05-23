package com.eaglebank.service;

import com.eaglebank.domain.BankAccount;
import com.eaglebank.domain.User;
import com.eaglebank.dto.request.AccountCreateRequest;
import com.eaglebank.dto.response.AccountResponse;
import com.eaglebank.dto.request.AccountUpdateRequest;
import com.eaglebank.dto.response.ListBankAccountsResponse;
import com.eaglebank.exception.ForbiddenException;
import com.eaglebank.exception.NotFoundException;
import com.eaglebank.mapper.AccountMapper;
import com.eaglebank.repository.AccountRepository;
import com.eaglebank.repository.UserRepository;
import com.eaglebank.security.CustomUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccountMapper accountMapper;

    @Transactional
    public AccountResponse createAccount(AccountCreateRequest request) {
        User user = getCurrentUser();

        BankAccount account = BankAccount.builder()
                .user(user)
                .name(request.getName())
                .accountType(request.getAccountType())
                .accountNumber(generateUniqueAccountNumber())
                .balance(BigDecimal.ZERO)
                .sortCode("10-10-10")
                .currency("GBP")
                .build();

        BankAccount savedAccount = accountRepository.save(account);
        return accountMapper.toResponse(savedAccount);
    }

    @Transactional(readOnly = true)
    public ListBankAccountsResponse listAccounts() {
        User user = getCurrentUser();
        // In a real app, we might filter by user, but the spec says "List accounts"
        // Let's assume it's list for the current user.
        List<BankAccount> accounts = accountRepository.findAll().stream()
                .filter(a -> a.getUser().getId().equals(user.getId()))
                .collect(Collectors.toList());

        return ListBankAccountsResponse.builder()
                .accounts(accounts.stream()
                        .map(accountMapper::toResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    @Transactional(readOnly = true)
    public AccountResponse fetchAccountByAccountNumber(String accountNumber) {
        BankAccount account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new NotFoundException("Bank account was not found"));
        checkOwnership(account);
        return accountMapper.toResponse(account);
    }

    @Transactional
    public AccountResponse updateAccount(String accountNumber, AccountUpdateRequest request) {
        BankAccount account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new NotFoundException("Bank account was not found"));
        checkOwnership(account);

        if (request.getName() != null) {
            account.setName(request.getName());
        }
        if (request.getAccountType() != null) {
            account.setAccountType(request.getAccountType());
        }

        BankAccount updatedAccount = accountRepository.save(account);
        return accountMapper.toResponse(updatedAccount);
    }

    @Transactional
    public void deleteAccount(String accountNumber) {
        BankAccount account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new NotFoundException("Bank account was not found"));
        checkOwnership(account);
        accountRepository.delete(account);
    }

    private User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomUserPrincipal customPrincipal) {
            return userRepository.findById(customPrincipal.getId())
                    .orElseThrow(() -> new NotFoundException("Authenticated user not found"));
        }
        throw new ForbiddenException("User not authenticated");
    }

    private void checkOwnership(BankAccount account) {
        User currentUser = getCurrentUser();
        if (!account.getUser().getId().equals(currentUser.getId())) {
            throw new ForbiddenException("The user is not allowed to access the bank account details");
        }
    }

    private String generateUniqueAccountNumber() {
        Random random = new Random();
        String accountNumber;
        do {
            int number = random.nextInt(1000000); // 0 to 999999
            accountNumber = String.format("01%06d", number);
        } while (accountRepository.existsByAccountNumber(accountNumber));
        return accountNumber;
    }
}
