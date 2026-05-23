package com.eaglebank.dto.request;

import com.eaglebank.dto.response.AddressDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Update user details")
public class UserUpdateRequest {
    @Schema(description = "User's full name", example = "Updated User")
    private String name;

    @Valid
    private AddressDTO address;

    @Pattern(regexp = "^\\+[1-9]\\d{1,14}$")
    @Schema(description = "User's phone number", example = "+441234567890")
    private String phoneNumber;

    @Email
    @Schema(description = "User's email address", example = "updated@example.com")
    private String email;
}
