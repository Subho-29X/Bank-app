package com.fullstack.Bank.controller;

import com.fullstack.Bank.dto.BankDTOs.*;
import com.fullstack.Bank.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bank")
public class BankController {

    @Autowired
    private BankService bankService;

    // POST /api/bank/accounts  — create new account
    @PostMapping("/accounts")
    public ResponseEntity<ApiResponse<AccountResponse>> createAccount(
            @RequestBody CreateAccountRequest request) {
        try {
            AccountResponse account = bankService.createAccount(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Account created successfully.", account));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // GET /api/bank/accounts/{accountNumber}  — get account details
    @GetMapping("/accounts/{accountNumber}")
    public ResponseEntity<ApiResponse<AccountResponse>> getAccount(
            @PathVariable String accountNumber) {
        try {
            AccountResponse account = bankService.getAccountByNumber(accountNumber);
            return ResponseEntity.ok(new ApiResponse<>(true, "Account fetched.", account));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // GET /api/bank/accounts/{accountNumber}/balance  — check balance only
    @GetMapping("/accounts/{accountNumber}/balance")
    public ResponseEntity<ApiResponse<Double>> getBalance(
            @PathVariable String accountNumber) {
        try {
            Double balance = bankService.getBalance(accountNumber);
            return ResponseEntity.ok(new ApiResponse<>(true, "Balance fetched.", balance));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // POST /api/bank/accounts/{accountNumber}/deposit  — deposit money
    @PostMapping("/accounts/{accountNumber}/deposit")
    public ResponseEntity<ApiResponse<TransactionResponse>> deposit(
            @PathVariable String accountNumber,
            @RequestBody TransactionRequest request) {
        try {
            TransactionResponse tx = bankService.deposit(accountNumber, request);
            return ResponseEntity.ok(new ApiResponse<>(true, "Deposit successful.", tx));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // POST /api/bank/accounts/{accountNumber}/withdraw  — withdraw money
    @PostMapping("/accounts/{accountNumber}/withdraw")
    public ResponseEntity<ApiResponse<TransactionResponse>> withdraw(
            @PathVariable String accountNumber,
            @RequestBody TransactionRequest request) {
        try {
            TransactionResponse tx = bankService.withdraw(accountNumber, request);
            return ResponseEntity.ok(new ApiResponse<>(true, "Withdrawal successful.", tx));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // GET /api/bank/accounts/{accountNumber}/transactions  — transaction history
    @GetMapping("/accounts/{accountNumber}/transactions")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getTransactions(
            @PathVariable String accountNumber) {
        try {
            List<TransactionResponse> txList = bankService.getTransactionHistory(accountNumber);
            return ResponseEntity.ok(new ApiResponse<>(true, "Transactions fetched.", txList));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}