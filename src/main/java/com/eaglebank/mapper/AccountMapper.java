package com.eaglebank.mapper;

import com.eaglebank.domain.BankAccount;
import com.eaglebank.dto.response.AccountResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "createdTimestamp", source = "createdAt")
    @Mapping(target = "updatedTimestamp", source = "updatedAt")
    AccountResponse toResponse(BankAccount account);
}
