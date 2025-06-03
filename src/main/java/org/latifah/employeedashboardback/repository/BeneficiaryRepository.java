package org.latifah.employeedashboardback.repository;

import org.latifah.employeedashboardback.model.Beneficiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BeneficiaryRepository extends JpaRepository<Beneficiary, String> {
    List<Beneficiary> findByClientId(Long clientId);
    Optional<Beneficiary> findByRib(String rib); // Changed to Optional<Beneficiary>
}
