package org.latifah.employeedashboardback.repository;

import org.latifah.employeedashboardback.model.SuspendedService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SuspendedServiceRepository extends JpaRepository<SuspendedService, Long> {
    List<SuspendedService> findByUserId(Long userId);
}

