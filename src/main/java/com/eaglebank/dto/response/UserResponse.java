package com.eaglebank.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "The user details")
public class UserResponse {
    @Schema(description = "Unique identifier of the user", example = "usr-abc123")
    private String id;

    @Schema(description = "User's full name", example = "Test User")
    private String name;

    private AddressDTO address;

    @Schema(description = "User's phone number", example = "+441234567890")
    private String phoneNumber;

    @Schema(description = "User's email address", example = "test@example.com")
    private String email;

    @Schema(description = "Timestamp when the user was created")
    private Instant createdTimestamp;

    @Schema(description = "Timestamp of the last update")
    private Instant updatedTimestamp;
}
