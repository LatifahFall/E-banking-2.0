package org.latifah.employeedashboardback.repository;

import org.latifah.employeedashboardback.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    long countBySuccessTrue();
}

