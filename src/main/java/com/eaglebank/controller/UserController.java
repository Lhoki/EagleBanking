package com.eaglebank.controller;

import com.eaglebank.dto.request.UserCreateRequest;
import com.eaglebank.dto.request.UserUpdateRequest;
import com.eaglebank.dto.response.UserResponse;
import com.eaglebank.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users")
@Tag(name = "user", description = "Manage a user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create a new user",
            description = "Create a new user",
            operationId = "createUser"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "User has been created successfully")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid details supplied")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "An unexpected error occurred")
    public UserResponse createUser(@Valid @RequestBody UserCreateRequest request) {
        return userService.createUser(request);
    }

    @GetMapping("/{userId}")
    @Operation(
            summary = "Fetch user by ID",
            description = "Fetch user by ID.",
            operationId = "fetchUserByID",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "The user details")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "The request didn't supply all the necessary data")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Access token is missing or invalid")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "The user is not allowed to access the transaction")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User was not found")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "An unexpected error occurred")
    public UserResponse getUser(@PathVariable String userId) {
        return userService.getUser(userId);
    }

    @PatchMapping("/{userId}")
    @Operation(
            summary = "Update user by ID",
            description = "Update user by ID.",
            operationId = "updateUserByID",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public UserResponse updateUser(@PathVariable String userId, @Valid @RequestBody UserUpdateRequest request) {
        return userService.updateUser(userId, request);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Delete user by ID",
            description = "Delete user by ID.",
            operationId = "deleteUserByID",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public void deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
    }
}
