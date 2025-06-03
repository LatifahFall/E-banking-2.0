package org.latifah.employeedashboardback.service;

import org.latifah.employeedashboardback.repository.SuspendedServiceRepository;
import org.latifah.employeedashboardback.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class SuspensionService {

    private final UserRepository userRepository;
    private final SuspendedServiceRepository suspendedServiceRepository;

    public SuspensionService(UserRepository userRepository, SuspendedServiceRepository suspendedServiceRepository) {
        this.userRepository = userRepository;
        this.suspendedServiceRepository = suspendedServiceRepository;
    }
}

