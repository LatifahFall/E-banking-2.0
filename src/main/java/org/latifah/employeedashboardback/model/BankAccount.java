package org.latifah.employeedashboardback.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE" , length = 10)
@Table(name = "accounts")
public class BankAccount {
    @Id
    private String id;

    @PrePersist
    public void generateId() {
        if (this.id == null) {
            this.id = java.util.UUID.randomUUID().toString();
        }
    }
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date createAt;

    // Encrypted, stored in DB
    @Column(name = "accountnumber")
    private String accountNumber;

    // Human-readable format, for display/search
    @Transient
    private String rawAccountNumber;


    private String type; // courant / épargne
    private double balance;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    private List<AccountOperation> accountOperations;

    // getters and setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public Date getCreateAt() {
        return createAt;
    }
    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
    public String getAccountNumber() {
        return accountNumber;
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
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public List<AccountOperation> getAccountOperations() { return accountOperations; }
    public void setAccountOperations(List<AccountOperation> transactions) { this.accountOperations = accountOperations; }
    public String getRawAccountNumber() {
        return rawAccountNumber;
    }
    public void setRawAccountNumber(String rawAccountNumber) {
        this.rawAccountNumber = rawAccountNumber;
    }
}
