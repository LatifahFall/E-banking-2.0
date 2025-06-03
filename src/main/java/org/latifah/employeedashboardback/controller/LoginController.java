package org.latifah.employeedashboardback.controller;

import org.latifah.employeedashboardback.model.Role;
import org.latifah.employeedashboardback.service.AuditService;
import org.latifah.employeedashboardback.service.Infobip2FAService;
import org.latifah.employeedashboardback.service.UserService;
import org.latifah.employeedashboardback.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")

public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserService userService;
    @Autowired
    private Infobip2FAService twoFAService;
    @Autowired
    private AuditService auditService;
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        try {
            Long userId = userService.getUserIdByUsername(username);
            logger.info("Login attempt for username: {}", username);
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            Role role = userService.getUserRoleByUsername(username);
            // Check if user is CLIENT and requires 2FA
            if (role == Role.CLIENT) {
                // Send 2FA PIN instead of immediate login
                boolean pinSent = twoFAService.sendPin(username);
                if (pinSent) {
                    Map<String, String> response = new HashMap<>();
                    response.put("message", "Code de vérification envoyé. Veuillez vérifier votre téléphone.");
                    response.put("requires2FA", "true");
                    response.put("username", username);
                    auditService.logAction("LOGIN_SUCCESS", "USER", userId.toString(), Map.of("username", username), true);
                    return ResponseEntity.ok(response);

                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Erreur lors de l'envoi du code de vérification");
                }
            } else {
                // Non-CLIENT users login directly
                String token = jwtUtil.generateToken(username);
                //Long userId = userService.getUserIdByUsername(username);

                Map<String, String> response = new HashMap<>();
                response.put("token", token);
                response.put("role", role.name());
                response.put("clientId", userId.toString());
                response.put("message", "Authentification réussie !");
                auditService.logAction("LOGIN_SUCCESS", "USER", userId.toString(), Map.of("username", username), true);
                return ResponseEntity.ok(response);

            }
        } catch (Exception e) {
            logger.error("Login failed for username: {}, error: {}", username, e.getMessage());
            auditService.logAction("LOGIN_FAILURE", "USER", null, Map.of("username", username), false);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Identifiants invalides");
        }
    }

    @PostMapping("/verify-2fa")
    public ResponseEntity<?> verify2FA(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String pin = body.get("pin");

        try {
            logger.info("2FA verification attempt for username: {}", username);

            if (username == null || pin == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Nom d'utilisateur et code PIN requis");
            }

            boolean isValidPin = twoFAService.verifyPin(username, pin);

            if (isValidPin) {
                // Generate JWT token after successful 2FA verification
                String token = jwtUtil.generateToken(username);
                Role role = userService.getUserRoleByUsername(username);
                Long userId = userService.getUserIdByUsername(username);

                Map<String, String> response = new HashMap<>();
                response.put("token", token);
                response.put("role", role.name());
                response.put("clientId", userId.toString());
                response.put("message", "Authentification réussie !");
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Code PIN invalide ou expiré");
            }

        } catch (Exception e) {
            logger.error("2FA verification failed for username: {}, error: {}", username, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la vérification du code PIN");
        }
    }

    @PostMapping("/resend-2fa")
    public ResponseEntity<?> resend2FA(@RequestBody Map<String, String> body) {
        String username = body.get("username");

        try {
            logger.info("Resend 2FA request for username: {}", username);

            if (username == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Nom d'utilisateur requis");
            }

            boolean pinSent = twoFAService.sendPin(username);

            if (pinSent) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Nouveau code de vérification envoyé");
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Erreur lors de l'envoi du code de vérification");
            }

        } catch (Exception e) {
            logger.error("Resend 2FA failed for username: {}, error: {}", username, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l'envoi du code de vérification");
        }
    }

    @GetMapping("/role")
    public ResponseEntity<?> getRole(Authentication authentication) {
        try {
            String username = authentication.getName();
            logger.info("Fetching role for username: {}", username);
            Role role = userService.getUserRoleByUsername(username);
            Map<String, String> response = new HashMap<>();
            response.put("role", role.name());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            logger.error("Role not found for username: {}", authentication.getName());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Role not found");
        } catch (Exception e) {
            logger.error("Error fetching role: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching role");
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        try {
            String username = authentication.getName();
            logger.info("Fetching user profile for username: {}", username);
            Long userId = userService.getUserIdByUsername(username);
            Map<String, String> response = new HashMap<>();
            response.put("clientId", userId.toString());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            logger.error("User not found for username: {}", authentication.getName());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (Exception e) {
            logger.error("Error fetching user profile: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching user profile");
        }
    }
}