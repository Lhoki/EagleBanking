package com.eaglebank.mapper;

import com.eaglebank.domain.BankAccount;
import com.eaglebank.dto.response.AccountResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-23T17:15:11+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.11-ea (Ubuntu)"
)
@Component
public class AccountMapperImpl implements AccountMapper {

    @Override
    public AccountResponse toResponse(BankAccount account) {
        if ( account == null ) {
            return null;
        }

        AccountResponse.AccountResponseBuilder accountResponse = AccountResponse.builder();

        accountResponse.createdTimestamp( account.getCreatedAt() );
        accountResponse.updatedTimestamp( account.getUpdatedAt() );
        accountResponse.accountNumber( account.getAccountNumber() );
        accountResponse.sortCode( account.getSortCode() );
        accountResponse.name( account.getName() );
        accountResponse.accountType( account.getAccountType() );
        accountResponse.balance( account.getBalance() );
        accountResponse.currency( account.getCurrency() );

        return accountResponse.build();
    }
}
