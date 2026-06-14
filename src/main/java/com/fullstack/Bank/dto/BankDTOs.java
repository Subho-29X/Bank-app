package com.fullstack.Bank.dto;

// ── Request DTOs ──────────────────────────────────────────────

public class BankDTOs {

    // Create a new account
    public static class CreateAccountRequest {
        private String accountHolderName;
        private String email;
        private Double initialDeposit;

        public String getAccountHolderName() { return accountHolderName; }
        public void setAccountHolderName(String accountHolderName) { this.accountHolderName = accountHolderName; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public Double getInitialDeposit() { return initialDeposit; }
        public void setInitialDeposit(Double initialDeposit) { this.initialDeposit = initialDeposit; }
    }

    // Deposit / Withdraw
    public static class TransactionRequest {
        private Double amount;
        private String note;

        public Double getAmount() { return amount; }
        public void setAmount(Double amount) { this.amount = amount; }

        public String getNote() { return note; }
        public void setNote(String note) { this.note = note; }
    }

    // ── Response DTOs ─────────────────────────────────────────

    public static class AccountResponse {
        private Long id;
        private String accountNumber;
        private String accountHolderName;
        private String email;
        private Double balance;
        private String createdAt;

        public AccountResponse(Long id, String accountNumber, String accountHolderName,
                               String email, Double balance, String createdAt) {
            this.id = id;
            this.accountNumber = accountNumber;
            this.accountHolderName = accountHolderName;
            this.email = email;
            this.balance = balance;
            this.createdAt = createdAt;
        }

        public Long getId() { return id; }
        public String getAccountNumber() { return accountNumber; }
        public String getAccountHolderName() { return accountHolderName; }
        public String getEmail() { return email; }
        public Double getBalance() { return balance; }
        public String getCreatedAt() { return createdAt; }
    }

    public static class TransactionResponse {
        private Long transactionId;
        private String type;
        private Double amount;
        private Double balanceAfter;
        private String note;
        private String timestamp;

        public TransactionResponse(Long transactionId, String type, Double amount,
                                   Double balanceAfter, String note, String timestamp) {
            this.transactionId = transactionId;
            this.type = type;
            this.amount = amount;
            this.balanceAfter = balanceAfter;
            this.note = note;
            this.timestamp = timestamp;
        }

        public Long getTransactionId() { return transactionId; }
        public String getType() { return type; }
        public Double getAmount() { return amount; }
        public Double getBalanceAfter() { return balanceAfter; }
        public String getNote() { return note; }
        public String getTimestamp() { return timestamp; }
    }

    public static class ApiResponse<T> {
        private boolean success;
        private String message;
        private T data;

        public ApiResponse(boolean success, String message, T data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public T getData() { return data; }
    }
}