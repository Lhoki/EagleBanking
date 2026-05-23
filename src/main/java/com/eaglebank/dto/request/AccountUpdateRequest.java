package com.eaglebank.dto.request;

import com.eaglebank.domain.AccountType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Update bank account details for the user")
public class AccountUpdateRequest {
    @Schema(description = "Name of the bank account", example = "Updated Bank Account")
    private String name;

    @Schema(description = "Type of the bank account", example = "personal")
    private AccountType accountType;
}
