package com.eaglebank.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {

    @NotBlank
    @Schema(description = "First line of address", example = "1 Test Street")
    private String line1;

    @Schema(description = "Second line of address", example = "Test Building")
    private String line2;

    @Schema(description = "Third line of address", example = "Test District")
    private String line3;

    @NotBlank
    @Schema(description = "Town of address", example = "London")
    private String town;

    @NotBlank
    @Schema(description = "County of address", example = "Greater London")
    private String county;

    @NotBlank
    @Schema(description = "Postcode of address", example = "SW1A 1AA")
    private String postcode;
}
