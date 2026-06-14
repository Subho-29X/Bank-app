package com.fullstack.Bank.dao;

import com.fullstack.Bank.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNumber(String accountNumber);
    Optional<Account> findByEmail(String email);
    boolean existsByAccountNumber(String accountNumber);
    boolean existsByEmail(String email);
}