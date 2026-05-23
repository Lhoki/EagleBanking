package com.eaglebank.repository;

import com.eaglebank.domain.BankAccount;
import com.eaglebank.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findByAccount(BankAccount account);
    Optional<Transaction> findByAccountAndTransactionId(BankAccount account, String transactionId);
}
