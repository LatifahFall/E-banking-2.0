package org.latifah.employeedashboardback.dto;

import java.util.List;
import org.latifah.employeedashboardback.model.BankAccount;
import org.latifah.employeedashboardback.security.EncryptionUtil;

public class AccountSummaryDTO {
    private String accountNumber;
    private String type;
    private double balance;
    private List<AccountOperationDTO> accountOperations;

    // constructeur, getters/setters
    public AccountSummaryDTO() {
        super();
    }
    public AccountSummaryDTO(String accountNumber, String type, double balance, List<TransactionDTO> accountOperations) {
        super();
    }

    // Static method to convert from entity to DTO
    public static AccountSummaryDTO fromEntity(BankAccount account) {
        AccountSummaryDTO dto = new AccountSummaryDTO();
        dto.setAccountNumber(EncryptionUtil.decrypt(account.getAccountNumber()));
        dto.setType(account.getType());
        dto.setBalance(account.getBalance());
        return dto;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public double getBalance() {
        return balance;
    }
    public void setBalance(double balance) {
        this.balance = balance;
    }
    public List<AccountOperationDTO> getAccountOperations() {
        return accountOperations;
    }
    public void setAccountOperations(List<AccountOperationDTO> accountOperations) {
        this.accountOperations = accountOperations;
    }
 }

