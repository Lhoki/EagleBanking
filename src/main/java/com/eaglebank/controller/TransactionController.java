package com.eaglebank.controller;

import com.eaglebank.dto.request.TransactionCreateRequest;
import com.eaglebank.dto.response.ListTransactionsResponse;
import com.eaglebank.dto.response.TransactionResponse;
import com.eaglebank.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/accounts/{accountNumber}/transactions")
@Tag(name = "transaction", description = "Manage transactions on a bank account")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create a transaction",
            description = "Create a transaction",
            operationId = "createTransaction",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public TransactionResponse createTransaction(
            @PathVariable String accountNumber,
            @Valid @RequestBody TransactionCreateRequest request
    ) {
        return transactionService.createTransaction(accountNumber, request);
    }

    @GetMapping
    @Operation(
            summary = "List transactions",
            description = "List transactions",
            operationId = "listAccountTransaction",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ListTransactionsResponse listTransactions(@PathVariable String accountNumber) {
        return transactionService.listAccountTransactions(accountNumber);
    }

    @GetMapping("/{transactionId}")
    @Operation(
            summary = "Fetch transaction by ID",
            description = "Fetch transaction by ID.",
            operationId = "fetchAccountTransactionByID",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public TransactionResponse fetchTransactionById(
            @PathVariable String accountNumber,
            @PathVariable String transactionId
    ) {
        return transactionService.fetchTransactionById(accountNumber, transactionId);
    }
}
