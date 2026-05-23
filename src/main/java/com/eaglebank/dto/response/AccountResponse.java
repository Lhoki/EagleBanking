package com.eaglebank.dto.response;

import com.eaglebank.domain.AccountType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "The bank account details")
public class AccountResponse {
    @Schema(description = "Account number of the bank account", example = "01234567")
    private String accountNumber;

    @Schema(description = "Sort code of the bank account", example = "10-10-10")
    private String sortCode;

    @Schema(description = "Name of the bank account", example = "Personal Bank Account")
    private String name;

    @Schema(description = "Type of the bank account", example = "personal")
    private AccountType accountType;

    @Schema(description = "Current balance of the account", example = "0.00")
    private BigDecimal balance;

    @Schema(description = "Currency of the account", example = "GBP")
    private String currency;

    @Schema(description = "Timestamp when the account was created")
    private Instant createdTimestamp;

    @Schema(description = "Timestamp when the account was last updated")
    private Instant updatedTimestamp;
}
