package com.eaglebank.mapper;

import com.eaglebank.domain.Address;
import com.eaglebank.domain.User;
import com.eaglebank.dto.request.UserCreateRequest;
import com.eaglebank.dto.response.AddressDTO;
import com.eaglebank.dto.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    User toEntity(UserCreateRequest request);

    Address toAddress(AddressDTO addressDto);

    @Mapping(target = "id", source = "id", qualifiedByName = "uuidToUserString")
    @Mapping(target = "createdTimestamp", source = "createdAt")
    @Mapping(target = "updatedTimestamp", source = "updatedAt")
    UserResponse toResponse(User user);

    @Named("uuidToUserString")
    default String uuidToUserString(UUID uuid) {
        return uuid == null ? null : "usr-" + uuid.toString().replace("-", "");
    }
}
