package auth;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditService {
    public void logAction(String username, String action) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String logMessage = String.format("[%s] %s: %s", timestamp, username, action);
        System.out.println("" + logMessage);
    }

    public void logProductAction(String username, String action, String productDetails) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String logMessage = String.format("[%s] %s: %s - %s", timestamp, username, action, productDetails);
        System.out.println("" + logMessage);
    }
}