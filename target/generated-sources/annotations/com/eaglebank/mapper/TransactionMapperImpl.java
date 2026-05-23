package com.eaglebank.mapper;

import com.eaglebank.domain.BankAccount;
import com.eaglebank.domain.Transaction;
import com.eaglebank.domain.User;
import com.eaglebank.dto.response.TransactionResponse;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-23T15:58:51+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.11-ea (Ubuntu)"
)
@Component
public class TransactionMapperImpl implements TransactionMapper {

    @Override
    public TransactionResponse toResponse(Transaction transaction) {
        if ( transaction == null ) {
            return null;
        }

        TransactionResponse.TransactionResponseBuilder transactionResponse = TransactionResponse.builder();

        transactionResponse.id( transaction.getTransactionId() );
        transactionResponse.userId( uuidToUserString( transactionAccountUserId( transaction ) ) );
        transactionResponse.createdTimestamp( transaction.getCreatedAt() );
        transactionResponse.amount( transaction.getAmount() );
        if ( transaction.getType() != null ) {
            transactionResponse.type( transaction.getType().name() );
        }
        transactionResponse.reference( transaction.getReference() );

        return transactionResponse.build();
    }

    private UUID transactionAccountUserId(Transaction transaction) {
        if ( transaction == null ) {
            return null;
        }
        BankAccount account = transaction.getAccount();
        if ( account == null ) {
            return null;
        }
        User user = account.getUser();
        if ( user == null ) {
            return null;
        }
        UUID id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
