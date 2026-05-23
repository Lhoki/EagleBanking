package com.eaglebank.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Create a new transaction")
public class TransactionCreateRequest {

    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("10000.0")
    @Schema(description = "Currency amount with up to two decimal places", example = "10.00")
    private BigDecimal amount;

    @NotBlank
    @Schema(description = "Currency of the transaction", example = "GBP")
    private String currency;

    @NotBlank
    @Schema(description = "Type of the transaction", example = "deposit")
    private String type;

    @Schema(description = "Reference for the transaction", example = "Refund for order #123")
    private String reference;
}
