package com.eaglebank.dto.response;

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
@Schema(description = "The transaction details")
public class TransactionResponse {
    @Schema(description = "Unique identifier of the transaction", example = "tan-A")
    private String id;

    @Schema(description = "Amount of the transaction", example = "10.00")
    private BigDecimal amount;

    @Schema(description = "Currency of the transaction", example = "GBP")
    private String currency;

    @Schema(description = "Type of the transaction", example = "deposit")
    private String type;

    @Schema(description = "Reference for the transaction", example = "Refund for order #123")
    private String reference;

    @Schema(description = "Unique identifier of the user", example = "usr-abc123")
    private String userId;

    @Schema(description = "Timestamp when the transaction was created")
    private Instant createdTimestamp;
}
