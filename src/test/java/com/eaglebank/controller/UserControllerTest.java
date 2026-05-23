package com.eaglebank.controller;

import com.eaglebank.dto.request.UserCreateRequest;
import com.eaglebank.dto.response.AddressDTO;
import com.eaglebank.dto.response.UserResponse;
import com.eaglebank.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    void createUser_Success() throws Exception {
        UserCreateRequest request = UserCreateRequest.builder()
                .name("Test User")
                .email("user@example.com")
                .password("SecurePassword123")
                .phoneNumber("+441234567890")
                .address(AddressDTO.builder()
                        .line1("123 Street")
                        .town("London")
                        .county("Greater London")
                        .postcode("SW1A 1AA")
                        .build())
                .build();

        when(userService.createUser(any(UserCreateRequest.class))).thenReturn(UserResponse.builder().build());

        mockMvc.perform(post("/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void createUser_InvalidPhoneNumber() throws Exception {
        UserCreateRequest request = UserCreateRequest.builder()
                .name("Test User")
                .email("user@example.com")
                .password("SecurePassword123")
                .phoneNumber("string") // Invalid phone number
                .address(AddressDTO.builder()
                        .line1("123 Street")
                        .town("London")
                        .county("Greater London")
                        .postcode("SW1A 1AA")
                        .build())
                .build();

        mockMvc.perform(post("/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
