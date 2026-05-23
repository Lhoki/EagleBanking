package com.eaglebank.mapper;

import com.eaglebank.domain.Address;
import com.eaglebank.domain.User;
import com.eaglebank.dto.request.UserCreateRequest;
import com.eaglebank.dto.response.AddressDTO;
import com.eaglebank.dto.response.UserResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-23T17:15:11+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.11-ea (Ubuntu)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toEntity(UserCreateRequest request) {
        if ( request == null ) {
            return null;
        }

        User.UserBuilder<?, ?> user = User.builder();

        user.name( request.getName() );
        user.phoneNumber( request.getPhoneNumber() );
        user.address( toAddress( request.getAddress() ) );
        user.email( request.getEmail() );

        return user.build();
    }

    @Override
    public Address toAddress(AddressDTO addressDto) {
        if ( addressDto == null ) {
            return null;
        }

        Address.AddressBuilder address = Address.builder();

        address.line1( addressDto.getLine1() );
        address.line2( addressDto.getLine2() );
        address.line3( addressDto.getLine3() );
        address.town( addressDto.getTown() );
        address.county( addressDto.getCounty() );
        address.postcode( addressDto.getPostcode() );

        return address.build();
    }

    @Override
    public UserResponse toResponse(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponse.UserResponseBuilder userResponse = UserResponse.builder();

        userResponse.id( uuidToUserString( user.getId() ) );
        userResponse.createdTimestamp( user.getCreatedAt() );
        userResponse.updatedTimestamp( user.getUpdatedAt() );
        userResponse.name( user.getName() );
        userResponse.address( addressToAddressDTO( user.getAddress() ) );
        userResponse.phoneNumber( user.getPhoneNumber() );
        userResponse.email( user.getEmail() );

        return userResponse.build();
    }

    protected AddressDTO addressToAddressDTO(Address address) {
        if ( address == null ) {
            return null;
        }

        AddressDTO.AddressDTOBuilder addressDTO = AddressDTO.builder();

        addressDTO.line1( address.getLine1() );
        addressDTO.line2( address.getLine2() );
        addressDTO.line3( address.getLine3() );
        addressDTO.town( address.getTown() );
        addressDTO.county( address.getCounty() );
        addressDTO.postcode( address.getPostcode() );

        return addressDTO.build();
    }
}
