package com.eaglebank.controller;

import com.eaglebank.dto.request.AccountCreateRequest;
import com.eaglebank.dto.request.AccountUpdateRequest;
import com.eaglebank.dto.response.AccountResponse;
import com.eaglebank.dto.response.ListBankAccountsResponse;
import com.eaglebank.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/accounts")
@Tag(name = "account", description = "Manage a bank account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create a new bank account",
            description = "Create a new bank account",
            operationId = "createAccount",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "201", description = "Bank Account has been created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid details supplied")
    @ApiResponse(responseCode = "401", description = "Access token is missing or invalid")
    @ApiResponse(responseCode = "403", description = "The user is not allowed to access the transaction")
    @ApiResponse(responseCode = "500", description = "An unexpected error occurred")
    public AccountResponse createAccount(@Valid @RequestBody AccountCreateRequest request) {
        return accountService.createAccount(request);
    }

    @GetMapping
    @Operation(
            summary = "List accounts",
            description = "List accounts",
            operationId = "listAccounts",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ListBankAccountsResponse listAccounts() {
        return accountService.listAccounts();
    }

    @GetMapping("/{accountNumber}")
    @Operation(
            summary = "Fetch account by account number",
            description = "Fetch account by account number.",
            operationId = "fetchAccountByAccountNumber",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public AccountResponse getAccount(@PathVariable String accountNumber) {
        return accountService.fetchAccountByAccountNumber(accountNumber);
    }

    @PatchMapping("/{accountNumber}")
    @Operation(
            summary = "Update account by account number",
            description = "Update account by account number.",
            operationId = "updateAccountByAccountNumber",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public AccountResponse updateAccount(
            @PathVariable String accountNumber,
            @Valid @RequestBody AccountUpdateRequest request
    ) {
        return accountService.updateAccount(accountNumber, request);
    }

    @DeleteMapping("/{accountNumber}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Delete account by account number",
            description = "Delete account by account number.",
            operationId = "deleteAccountByAccountNumber",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public void deleteAccount(@PathVariable String accountNumber) {
        accountService.deleteAccount(accountNumber);
    }
}
