package com.eaglebank.dto.request;

import com.eaglebank.dto.response.AddressDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Create a new user")
public class UserCreateRequest {

    @NotBlank
    @Schema(description = "User's full name", example = "Test User")
    private String name;

    @Valid
    @NotNull
    private AddressDTO address;

    @NotBlank
    @Pattern(regexp = "^\\+[1-9]\\d{1,14}$")
    @Schema(description = "User's phone number", example = "+441234567890")
    private String phoneNumber;

    @NotBlank
    @Email
    @Schema(description = "User's email address", example = "test@example.com")
    private String email;

    @NotBlank
    @Schema(description = "User's password", example = "SecurePassword123")
    private String password;
}
