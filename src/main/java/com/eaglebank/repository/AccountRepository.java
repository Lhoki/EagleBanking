package com.eaglebank.repository;

import com.eaglebank.domain.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<BankAccount, UUID> {
    Optional<BankAccount> findByAccountNumber(String accountNumber);
    boolean existsByAccountNumber(String accountNumber);
}
