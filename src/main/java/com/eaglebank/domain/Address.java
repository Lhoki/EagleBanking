package com.eaglebank.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @NotBlank
    @Column(nullable = false)
    private String line1;

    private String line2;

    private String line3;

    @NotBlank
    @Column(nullable = false)
    private String town;

    @NotBlank
    @Column(nullable = false)
    private String county;

    @NotBlank
    @Column(nullable = false)
    private String postcode;
}
