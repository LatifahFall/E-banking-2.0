package org.latifah.employeedashboardback.repository;

import org.latifah.employeedashboardback.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    Currency findByIsDefaultTrue();
    boolean existsByCodeISO(String codeISO);
    Optional<Currency> findByCodeISO(String codeISO);


}