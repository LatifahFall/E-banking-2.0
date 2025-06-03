package org.latifah.employeedashboardback.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.latifah.employeedashboardback.model.AccountStatus;

import java.util.Date;

public class SavingBankAccountDTO extends BankAccountDTO {
    private double interestRate;

    // Getters and Setters
    public double getInterestRate() { return interestRate; }
    public void setInterestRate(double interestRate) { this.interestRate = interestRate; }
}
