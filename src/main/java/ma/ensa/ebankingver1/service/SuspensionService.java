package ma.ensa.ebankingver1.service;

import jakarta.transaction.Transactional;
import ma.ensa.ebankingver1.model.BankService;
import ma.ensa.ebankingver1.model.SuspendedService;
import ma.ensa.ebankingver1.model.User;
import ma.ensa.ebankingver1.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SuspensionService {

    private final UserRepository userRepository;
    private final SuspendedServiceRepository suspendedServiceRepository;

    public SuspensionService(UserRepository userRepository, SuspendedServiceRepository suspendedServiceRepository) {
        this.userRepository = userRepository;
        this.suspendedServiceRepository = suspendedServiceRepository;
    }
    @Transactional
    public void suspendService(User user, BankService service) {
        if (!user.getServicesActifs().contains(service.name())) {
            throw new IllegalStateException("Service " + service + " not activated.");
        }
        SuspendedService suspendedService = new SuspendedService();
        suspendedService.setServiceName(service.name());
        suspendedService.setUser(user);
        suspendedService.setSuspensionDate(LocalDate.now());
        suspendedService.setReason("Requested via AI assistant.");
        suspendedService.setNotificationMessage("Service " + service.name() + " suspended.");
        suspendedServiceRepository.save(suspendedService);
        user.getServicesActifs().remove(service.name());
        userRepository.save(user);
    }
}
