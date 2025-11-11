package auth;

import model.User;
import java.util.HashMap;
import java.util.Map;

public class AuthService {
    private Map<String, User> users = new HashMap<>();
    private User currentUser = null;
    private AuditService auditService;

    public AuthService(AuditService auditService) {
        this.auditService = auditService;
        users.put("admin", new User("admin", "123"));
        users.put("user", new User("user", "456"));
    }

    public boolean login(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            auditService.logAction(username, "ВХОД в систему");
            return true;
        }
        auditService.logAction(username, "НЕУДАЧНАЯ попытка входа");
        return false;
    }

    public void logout() {
        if (currentUser != null) {
            auditService.logAction(currentUser.getUsername(), "ВЫХОД из системы");
            currentUser = null;
        }
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public String getCurrentUsername() {
        return currentUser != null ? currentUser.getUsername() : "Гость";
    }

    public User getCurrentUser() {
        return currentUser;
    }
}