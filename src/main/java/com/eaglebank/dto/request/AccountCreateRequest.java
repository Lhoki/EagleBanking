package com.eaglebank.dto.request;

import com.eaglebank.domain.AccountType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Create a new bank account for the user")
public class AccountCreateRequest {

    @NotBlank
    @Schema(description = "Name of the bank account", example = "Personal Bank Account")
    private String name;

    @NotNull
    @Schema(description = "Type of the bank account", example = "personal")
    private AccountType accountType;
}
