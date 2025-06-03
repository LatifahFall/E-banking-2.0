package org.latifah.employeedashboardback.service;

import org.latifah.employeedashboardback.dto.*;
import org.latifah.employeedashboardback.model.AccountOperation;
import org.latifah.employeedashboardback.model.BankAccount;
import org.latifah.employeedashboardback.model.Role;
import org.latifah.employeedashboardback.model.User;
import org.latifah.employeedashboardback.repository.*;

import org.latifah.employeedashboardback.security.EncryptionUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class EnrollmentService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
//    private final TransactionRepository transactionRepository;
    private final AccountOperationRepository accountOperationRepository;

    public EnrollmentService(
            UserRepository userRepository,
            AccountRepository accountRepository,
            AccountOperationRepository accountOperationRepository // ajouté ici
    ) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.accountOperationRepository = accountOperationRepository;
    }


    public long countClients() {
        return userRepository.countClients();
    }

    public long countAccounts() {
        return accountRepository.count();
    }

    private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int USERNAME_LETTERS = 3;
    private static final int USERNAME_DIGITS = 4;
    private static final Random RANDOM = new Random();

    private String generateUniqueUsername() {
        String username;
        do {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < USERNAME_LETTERS; i++) {
                sb.append(LETTERS.charAt(RANDOM.nextInt(LETTERS.length())));
            }
            for (int i = 0; i < USERNAME_DIGITS; i++) {
                sb.append(RANDOM.nextInt(10));
            }
            username = sb.toString();
        } while (userRepository.findByUsername(username).isPresent());
        return username;
    }
    private String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%&";
        StringBuilder password = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        return password.toString();
    }



    private static final String ACCOUNT_PREFIX = "ACC";

    private static final String SUPERVISOR_CODE = "supervisor";

    public boolean validateSupervisor(String code) {
        return SUPERVISOR_CODE.equals(code);
    }

    @Transactional
    public void enrollClient(EnrollmentRequest dto) {
        // Création de l'utilisateur
        User client = new User();
        client.setFirstName(dto.getFirstName());
        client.setLastName(dto.getLastName());
        client.setEmail(dto.getEmail());
        client.setTel(dto.getTel());
        client.setBirth_Date(LocalDate.parse(dto.getBirthDate()));
        client.setRole(Role.CLIENT);
        client.setCin(dto.getCin());
        // Username auto-généré
        String generatedUsername = generateUniqueUsername();
        String generatedPassword = generateRandomPassword(8);

        client.setUsername(generatedUsername);
        client.setPassword(generatedPassword);

        userRepository.save(client);

        // Génération du numéro lisible et chiffrement
        String rawAccNum = generateReadableAccountNumber();
        String encryptedAccNum = EncryptionUtil.encrypt(rawAccNum);

        // Création du compte
        BankAccount account = new BankAccount();
        account.setRawAccountNumber(rawAccNum);
        account.setAccountNumber(encryptedAccNum); // champ chiffré
        account.setType(dto.getAccountType());
        account.setBalance(dto.getBalance());
        account.setUser(client);

        accountRepository.save(account);

        System.out.println("Identifiants créés → username: " + generatedUsername + ", password: " + generatedPassword);

    }

    // Générateur de numéro de compte au format ACCXXXXXXX
    private String generateReadableAccountNumber() {
        int number = new Random().nextInt(9000000) + 1000000; // garantit 7 chiffres
        return "ACC" + number;
    }


//    @Transactional
//    public boolean updateClient(ClientUpdateRequest dto) {
//        // Vérifier le mot de passe du superviseur
//        final String SUPERVISOR_PASSWORD = "supervisor";
//        if (dto.getSupervisorPassword() == null || !SUPERVISOR_PASSWORD.equals(dto.getSupervisorPassword())) {
//            return false;
//        }
//
//        // Chercher le client par ID
//        Optional<User> opt = userRepository.findById(dto.getClientId());
//        if (opt.isEmpty()) return false;
//
//        // Mettre à jour les champs fournis
//        User client = opt.get();
//        if (dto.getNewFirstName() != null) client.setFirstName(dto.getNewFirstName());
//        if (dto.getNewLastName() != null) client.setLastName(dto.getNewLastName());
//        if (dto.getNewEmail() != null) client.setEmail(dto.getNewEmail());
//        if (dto.getNewTel() != null) client.setTel(dto.getNewTel());
//
//        // vérifier l’état de l'objet avant sauvegarde
//        System.out.println("Client mis à jour : " + client);
//
//        // Sauvegarder les changements
//        userRepository.save(client);
//        System.out.println("🔍 SELECT à la main : " + userRepository.findById(dto.getClientId()));
////        userRepository.flush();
//        return true;
//    }

    @Transactional
    public boolean updateClient(ClientUpdateRequest dto) {
        Optional<User> opt = userRepository.findById(dto.getClientId());
        if (opt.isEmpty()) return false;

        User client = opt.get();

        if (dto.getNewFirstName() != null) client.setFirstName(dto.getNewFirstName());
        if (dto.getNewLastName() != null) client.setLastName(dto.getNewLastName());
        if (dto.getNewEmail() != null) client.setEmail(dto.getNewEmail());
        if (dto.getNewTel() != null) client.setTel(dto.getNewTel());

        userRepository.save(client);
        return true;
    }



//    @Transactional
//    public boolean deleteClient(Long clientId) {
//        Optional<User> opt = userRepository.findById(clientId);
//        if (opt.isEmpty()) return false;
//
//        if (!transactionRepository.findByUserId(clientId).isEmpty()) return false;
//
//        User client = opt.get();
//        accountRepository.deleteByUserId(clientId); // méthode custom ou native query
//        userRepository.delete(client);
//        return true;
//    }

    @Transactional
    public boolean deleteClient(Long clientId) {
        Optional<User> opt = userRepository.findById(clientId);
        if (opt.isEmpty()) return false;

        // Vérifie s'il y a des opérations liées à cet utilisateur
        List<AccountOperation> ops = accountOperationRepository.findByBankAccount_User_Id(clientId);
        if (!ops.isEmpty()) return false;

        // Suppression des comptes avant utilisateur
        accountRepository.deleteByUserId(clientId);
        userRepository.delete(opt.get());
        return true;
    }

}
