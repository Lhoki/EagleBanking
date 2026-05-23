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
    @Schema(description = "First line of address")
    private String line1;

    @Schema(description = "Second line of address")
    private String line2;

    @Schema(description = "Third line of address")
    private String line3;

    @NotBlank
    @Schema(description = "Town of address")
    private String town;

    @NotBlank
    @Schema(description = "County of address")
    private String county;

    @NotBlank
    @Schema(description = "Postcode of address")
    private String postcode;
}
