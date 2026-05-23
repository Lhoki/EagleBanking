package com.eaglebank.service;

import com.eaglebank.domain.User;
import com.eaglebank.dto.request.UserCreateRequest;
import com.eaglebank.dto.request.UserUpdateRequest;
import com.eaglebank.dto.response.UserResponse;
import com.eaglebank.exception.ConflictException;
import com.eaglebank.exception.ForbiddenException;
import com.eaglebank.exception.NotFoundException;
import com.eaglebank.mapper.UserMapper;
import com.eaglebank.repository.AccountRepository;
import com.eaglebank.repository.UserRepository;
import com.eaglebank.security.CustomUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse createUser(UserCreateRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Email already exists: " + request.getEmail());
        }

        User user = userMapper.toEntity(request);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    @Cacheable(value = "users", key = "#userId")
    @Transactional(readOnly = true)
    public UserResponse getUser(String userId) {
        UUID uuid = extractUuid(userId);
        checkOwnership(uuid);

        User user = userRepository.findById(uuid)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        return userMapper.toResponse(user);
    }

    @CacheEvict(value = "users", key = "#userId")
    @Transactional
    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        UUID uuid = extractUuid(userId);
        checkOwnership(uuid);

        User user = userRepository.findById(uuid)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        if (request.getName() != null) {
            user.setName(request.getName());
        }
        if (request.getEmail() != null) {
            if (!user.getEmail().equals(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
                throw new ConflictException("Email already exists: " + request.getEmail());
            }
            user.setEmail(request.getEmail());
        }
        if (request.getPhoneNumber() != null) {
            user.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getAddress() != null) {
            user.setAddress(userMapper.toAddress(request.getAddress()));
        }

        User updatedUser = userRepository.save(user);
        return userMapper.toResponse(updatedUser);
    }

    @CacheEvict(value = "users", key = "#userId")
    @Transactional
    public void deleteUser(String userId) {
        UUID uuid = extractUuid(userId);
        checkOwnership(uuid);

        User user = userRepository.findById(uuid)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        // Check if user has accounts - spec says "A user cannot be deleted when they are associated with a bank account"
        // This is often handled by DB constraints, but let's check explicitly for a better error message (409)
        // Note: accountRepository doesn't have a countByUser, but we can check existence or just let DB fail.
        // Let's assume we want the 409 as per YAML.
        if (hasAccounts(user)) {
             throw new ConflictException("A user cannot be deleted when they are associated with a bank account");
        }

        userRepository.delete(user);
    }

    private boolean hasAccounts(User user) {
        // Simple check
        return accountRepository.findAll().stream().anyMatch(a -> a.getUser().getId().equals(user.getId()));
    }

    private UUID extractUuid(String userId) {
        if (userId == null || !userId.startsWith("usr-")) {
            throw new IllegalArgumentException("Invalid user ID format");
        }
        String hex = userId.substring(4);
        if (hex.length() != 32) {
             throw new IllegalArgumentException("Invalid user ID format");
        }
        // Reconstruct UUID format: 8-4-4-4-12
        String formattedUuid = hex.substring(0, 8) + "-" +
                hex.substring(8, 12) + "-" +
                hex.substring(12, 16) + "-" +
                hex.substring(16, 20) + "-" +
                hex.substring(20);
        return UUID.fromString(formattedUuid);
    }

    private void checkOwnership(UUID userId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomUserPrincipal customPrincipal) {
            if (!customPrincipal.getId().equals(userId)) {
                throw new ForbiddenException("The user is not allowed to access the transaction");
            }
        } else {
            throw new ForbiddenException("Authentication required");
        }
    }
}
