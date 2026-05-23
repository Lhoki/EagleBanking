package com.eaglebank.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to authenticate and receive a JWT token")
public class LoginRequest {

    @NotBlank
    @Email
    @Schema(description = "User's registered email", example = "john.doe@example.com")
    private String email;

    @NotBlank
    @Schema(description = "User's password", example = "SecurePassword123")
    private String password;
}
