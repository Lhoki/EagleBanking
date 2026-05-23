package com.eaglebank.mapper;

import com.eaglebank.domain.Transaction;
import com.eaglebank.dto.response.TransactionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(target = "id", source = "transactionId")
    @Mapping(target = "userId", source = "account.user.id", qualifiedByName = "uuidToUserString")
    @Mapping(target = "createdTimestamp", source = "createdAt")
    TransactionResponse toResponse(Transaction transaction);

    @Named("uuidToUserString")
    default String uuidToUserString(UUID uuid) {
        return uuid == null ? null : "usr-" + uuid.toString().replace("-", "");
    }
}
