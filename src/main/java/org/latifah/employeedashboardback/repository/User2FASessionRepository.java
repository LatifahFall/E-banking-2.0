package org.latifah.employeedashboardback.repository;

import org.latifah.employeedashboardback.model.User2FASession;

public interface User2FASessionRepository {
    void save(User2FASession session);
    User2FASession findByUsernameAndVerifiedFalse(String username);
    void deleteByUsername(String username);
    void markAsVerified(String username);
}
