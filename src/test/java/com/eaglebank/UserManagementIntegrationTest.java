package com.eaglebank;

import com.eaglebank.dto.request.UserCreateRequest;
import com.eaglebank.dto.request.UserUpdateRequest;
import com.eaglebank.dto.response.AddressDTO;
import com.eaglebank.dto.response.UserResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserManagementIntegrationTest extends ApiTestSupport {

    @Test
    void userLifecycleTest() throws Exception {
        String email = "lifecycle@example.com";
        String password = "Password123!";
        
        UserCreateRequest userRequest = UserCreateRequest.builder()
                .name("Lifecycle User")
                .email(email)
                .password(password)
                .phoneNumber("+447700900111")
                .address(AddressDTO.builder()
                        .line1("10 Downing Street")
                        .town("London")
                        .county("London")
                        .postcode("SW1A 2AA")
                        .build())
                .build();

        // Create user.
        String userResponseJson = mockMvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        
        UserResponse userResponse = objectMapper.readValue(userResponseJson, UserResponse.class);
        String userId = userResponse.getId();
        String token = getAuthToken(email, password);

        // Get user details.
        mockMvc.perform(get("/v1/users/" + userId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email));

        // Update user details.
        UserUpdateRequest updateRequest = UserUpdateRequest.builder()
                .name("Updated Lifecycle User")
                .build();

        mockMvc.perform(patch("/v1/users/" + userId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Lifecycle User"));

        // Delete user.
        mockMvc.perform(delete("/v1/users/" + userId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        // Verify user is deleted.
        mockMvc.perform(get("/v1/users/" + userId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized());
    }
}
