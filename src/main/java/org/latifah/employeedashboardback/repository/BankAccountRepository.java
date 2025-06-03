package org.latifah.employeedashboardback.repository;

import org.latifah.employeedashboardback.model.BankAccount;
import org.latifah.employeedashboardback.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
    List<BankAccount> findByClient(User client);
    List<BankAccount> findByClientId(Long clientId);
}
