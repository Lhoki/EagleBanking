package com.eaglebank;

import com.eaglebank.domain.AccountType;
import com.eaglebank.dto.request.AccountCreateRequest;
import com.eaglebank.dto.request.TransactionCreateRequest;
import com.eaglebank.dto.request.UserCreateRequest;
import com.eaglebank.dto.response.AccountResponse;
import com.eaglebank.dto.response.AddressDTO;
import com.eaglebank.dto.response.UserResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BankingFlowIntegrationTest extends ApiTestSupport {

    @Test
    void fullBankingFlowTest() throws Exception {
        UserCreateRequest userRequest = UserCreateRequest.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .password("Password123!")
                .phoneNumber("+447700900000")
                .address(AddressDTO.builder()
                        .line1("10 Downing Street")
                        .town("London")
                        .county("London")
                        .postcode("SW1A 2AA")
                        .build())
                .build();

        // Create user.
        MvcResult userResult = mockMvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andReturn();

        UserResponse userResponse = objectMapper.readValue(userResult.getResponse().getContentAsString(), UserResponse.class);
        assertThat(userResponse.getId()).isNotBlank();

        // Login / authenticate user.
        String token = getAuthToken("john.doe@example.com", "Password123!");
        assertThat(token).isNotBlank();

        // Create bank account.
        AccountCreateRequest accountRequest = AccountCreateRequest.builder()
                .name("John's Savings")
                .accountType(AccountType.personal)
                .build();

        MvcResult accountResult = mockMvc.perform(post("/v1/accounts")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John's Savings"))
                .andExpect(jsonPath("$.balance").value(0.0))
                .andReturn();

        AccountResponse accountResponse = objectMapper.readValue(accountResult.getResponse().getContentAsString(), AccountResponse.class);
        String accountNumber = accountResponse.getAccountNumber();
        assertThat(accountNumber).isNotBlank();

        // Deposit funds.
        TransactionCreateRequest depositRequest = TransactionCreateRequest.builder()
                .amount(new BigDecimal("100.00"))
                .currency("GBP")
                .type("deposit")
                .reference("Initial deposit")
                .build();

        mockMvc.perform(post("/v1/accounts/" + accountNumber + "/transactions")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(depositRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.amount").value(100.00))
                .andExpect(jsonPath("$.type").value("deposit"));

        // Verify account balance after deposit.
        mockMvc.perform(get("/v1/accounts/" + accountNumber)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(100.00));

        // Withdraw funds.
        TransactionCreateRequest withdrawalRequest = TransactionCreateRequest.builder()
                .amount(new BigDecimal("30.00"))
                .currency("GBP")
                .type("withdrawal")
                .reference("Cash withdrawal")
                .build();

        mockMvc.perform(post("/v1/accounts/" + accountNumber + "/transactions")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(withdrawalRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.amount").value(30.00))
                .andExpect(jsonPath("$.type").value("withdrawal"));

        // Verify account balance after withdrawal.
        mockMvc.perform(get("/v1/accounts/" + accountNumber)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(70.00));

        // View transactions.
        mockMvc.perform(get("/v1/accounts/" + accountNumber + "/transactions")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactions").isArray())
                .andExpect(jsonPath("$.transactions.length()").value(2));
    }
}
