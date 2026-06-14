package com.fullstack.Bank.service;

import com.fullstack.Bank.dao.AccountRepository;
import com.fullstack.Bank.dao.TransactionRepository;
import com.fullstack.Bank.dto.BankDTOs.*;
import com.fullstack.Bank.entity.Account;
import com.fullstack.Bank.entity.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class BankService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    // ── Create Account ────────────────────────────────────────

    @Transactional
    public AccountResponse createAccount(CreateAccountRequest request) {
        if (accountRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("An account with this email already exists.");
        }

        Double initialBalance = (request.getInitialDeposit() != null && request.getInitialDeposit() > 0)
                ? request.getInitialDeposit() : 0.0;

        Account account = new Account(
                generateAccountNumber(),
                request.getAccountHolderName(),
                request.getEmail(),
                initialBalance
        );
        Account saved = accountRepository.save(account);

        // Record initial deposit as a transaction
        if (initialBalance > 0) {
            Transaction tx = new Transaction(saved, Transaction.TransactionType.DEPOSIT,
                    initialBalance, initialBalance, "Initial deposit");
            transactionRepository.save(tx);
        }

        return toAccountResponse(saved);
    }

    // ── Get Account Details ───────────────────────────────────

    public AccountResponse getAccountByNumber(String accountNumber) {
        Account account = findAccountByNumber(accountNumber);
        return toAccountResponse(account);
    }

    // ── Get Balance ───────────────────────────────────────────

    public Double getBalance(String accountNumber) {
        Account account = findAccountByNumber(accountNumber);
        return account.getBalance();
    }

    // ── Deposit ───────────────────────────────────────────────

    @Transactional
    public TransactionResponse deposit(String accountNumber, TransactionRequest request) {
        if (request.getAmount() == null || request.getAmount() <= 0) {
            throw new RuntimeException("Deposit amount must be greater than zero.");
        }

        Account account = findAccountByNumber(accountNumber);
        double newBalance = account.getBalance() + request.getAmount();
        account.setBalance(newBalance);
        accountRepository.save(account);

        Transaction tx = new Transaction(
                account,
                Transaction.TransactionType.DEPOSIT,
                request.getAmount(),
                newBalance,
                request.getNote()
        );
        Transaction saved = transactionRepository.save(tx);

        return toTransactionResponse(saved);
    }

    // ── Withdraw ──────────────────────────────────────────────

    @Transactional
    public TransactionResponse withdraw(String accountNumber, TransactionRequest request) {
        if (request.getAmount() == null || request.getAmount() <= 0) {
            throw new RuntimeException("Withdrawal amount must be greater than zero.");
        }

        Account account = findAccountByNumber(accountNumber);

        if (account.getBalance() < request.getAmount()) {
            throw new RuntimeException("Insufficient balance. Available: " + account.getBalance());
        }

        double newBalance = account.getBalance() - request.getAmount();
        account.setBalance(newBalance);
        accountRepository.save(account);

        Transaction tx = new Transaction(
                account,
                Transaction.TransactionType.WITHDRAWAL,
                request.getAmount(),
                newBalance,
                request.getNote()
        );
        Transaction saved = transactionRepository.save(tx);

        return toTransactionResponse(saved);
    }

    // ── Transaction History ───────────────────────────────────

    public List<TransactionResponse> getTransactionHistory(String accountNumber) {
        Account account = findAccountByNumber(accountNumber);
        return transactionRepository
                .findByAccountIdOrderByTimestampDesc(account.getId())
                .stream()
                .map(this::toTransactionResponse)
                .collect(Collectors.toList());
    }

    // ── Helpers ───────────────────────────────────────────────

    private Account findAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found: " + accountNumber));
    }

    private String generateAccountNumber() {
        String number;
        do {
            number = "ACC" + (100000000 + new Random().nextInt(900000000));
        } while (accountRepository.existsByAccountNumber(number));
        return number;
    }

    private AccountResponse toAccountResponse(Account a) {
        return new AccountResponse(
                a.getId(),
                a.getAccountNumber(),
                a.getAccountHolderName(),
                a.getEmail(),
                a.getBalance(),
                a.getCreatedAt() != null ? a.getCreatedAt().toString() : null
        );
    }

    private TransactionResponse toTransactionResponse(Transaction t) {
        return new TransactionResponse(
                t.getId(),
                t.getType().name(),
                t.getAmount(),
                t.getBalanceAfter(),
                t.getNote(),
                t.getTimestamp() != null ? t.getTimestamp().toString() : null
        );
    }
}