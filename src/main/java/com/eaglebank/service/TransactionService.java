package com.eaglebank.service;

import com.eaglebank.domain.BankAccount;
import com.eaglebank.domain.Transaction;
import com.eaglebank.domain.TransactionType;
import com.eaglebank.dto.request.TransactionCreateRequest;
import com.eaglebank.dto.response.ListTransactionsResponse;
import com.eaglebank.dto.response.TransactionResponse;
import com.eaglebank.exception.ForbiddenException;
import com.eaglebank.exception.NotFoundException;
import com.eaglebank.exception.ValidationException;
import com.eaglebank.mapper.TransactionMapper;
import com.eaglebank.repository.AccountRepository;
import com.eaglebank.repository.TransactionRepository;
import com.eaglebank.security.CustomUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private static final String TRANSACTION_ID_PATTERN = "^tan-[A-Za-z0-9]$";
    private static final char[] TRANSACTION_ID_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionMapper transactionMapper;

    @Transactional
    public TransactionResponse createTransaction(String accountNumber, TransactionCreateRequest request) {
        BankAccount account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new NotFoundException("Bank account was not found"));

        validateUserAccess(account);

        TransactionType type = TransactionType.valueOf(request.getType());
        BigDecimal amount = request.getAmount();

        if (type == TransactionType.withdrawal && account.getBalance().compareTo(amount) < 0) {
            throw new ValidationException("Insufficient funds to process transaction");
        }

        Transaction transaction = Transaction.builder()
                .account(account)
                .transactionId(generateTransactionId(account))
                .amount(amount)
                .type(type)
                .reference(request.getReference())
                .build();

        if (type == TransactionType.deposit) {
            account.setBalance(account.getBalance().add(amount));
        } else {
            account.setBalance(account.getBalance().subtract(amount));
        }

        accountRepository.save(account);
        Transaction savedTransaction = transactionRepository.save(transaction);
        return transactionMapper.toResponse(savedTransaction);
    }

    @Transactional(readOnly = true)
    public ListTransactionsResponse listAccountTransactions(String accountNumber) {
        BankAccount account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new NotFoundException("Bank account was not found"));

        validateUserAccess(account);

        List<Transaction> transactions = transactionRepository.findByAccount(account);
        return ListTransactionsResponse.builder()
                .transactions(transactions.stream()
                        .map(transactionMapper::toResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    @Transactional(readOnly = true)
    public TransactionResponse fetchTransactionById(String accountNumber, String transactionId) {
        BankAccount account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new NotFoundException("Bank account was not found"));

        validateUserAccess(account);

        String validatedTransactionId = validateTransactionId(transactionId);
        Transaction transaction = transactionRepository.findByAccountAndTransactionId(account, validatedTransactionId)
                .orElseThrow(() -> new NotFoundException("Transaction was not found"));

        return transactionMapper.toResponse(transaction);
    }

    private void validateUserAccess(BankAccount account) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomUserPrincipal customPrincipal) {
            if (!customPrincipal.getId().equals(account.getUser().getId())) {
                throw new ForbiddenException("The user is not allowed to access the transaction");
            }
        } else {
            throw new ForbiddenException("Authentication required");
        }
    }

    private String validateTransactionId(String transactionId) {
        if (transactionId == null || !transactionId.matches(TRANSACTION_ID_PATTERN)) {
            throw new ValidationException("Invalid transaction ID format");
        }
        return transactionId;
    }

    private String generateTransactionId(BankAccount account) {
        Set<String> existingTransactionIds = transactionRepository.findByAccount(account).stream()
                .map(Transaction::getTransactionId)
                .collect(Collectors.toSet());

        for (char idChar : TRANSACTION_ID_CHARS) {
            String transactionId = "tan-" + idChar;
            if (!existingTransactionIds.contains(transactionId)) {
                return transactionId;
            }
        }

        throw new ValidationException("No transaction IDs are available for this account");
    }
}
