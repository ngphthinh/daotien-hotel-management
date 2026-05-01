package iuh.fit.se.group1.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Singleton manager to track active user sessions
 * Prevents duplicate logins from different connections
 */
public class ActiveUsersManager {
    private static final Logger log = LoggerFactory.getLogger(ActiveUsersManager.class);
    private static ActiveUsersManager instance;
    
    // username -> login timestamp
    private final ConcurrentMap<String, Long> activeUsers = new ConcurrentHashMap<>();
    
    private ActiveUsersManager() {
    }
    
    public static synchronized ActiveUsersManager getInstance() {
        if (instance == null) {
            instance = new ActiveUsersManager();
        }
        return instance;
    }
    
    /**
     * Check if user is already logged in
     */
    public boolean isUserLoggedIn(String username) {
        return activeUsers.containsKey(username);
    }
    
    /**
     * Register user as logged in
     * @return true if successfully registered, false if already logged in
     */
    public boolean registerLogin(String username) {
        if (isUserLoggedIn(username)) {
            log.warn("Attempt to login with already active user: {}", username);
            return false;
        }
        
        long loginTime = System.currentTimeMillis();
        activeUsers.put(username, loginTime);
        log.info("User {} logged in at {}", username, loginTime);
        return true;
    }
    
    /**
     * Unregister user logout
     */
    public void registerLogout(String username) {
        if (activeUsers.remove(username) != null) {
            log.info("User {} logged out", username);
        }
    }
    
    /**
     * Get all active users
     */
    public int getActiveUserCount() {
        return activeUsers.size();
    }
    
    /**
     * Clear all active sessions (for testing or shutdown)
     */
    public void clearAllSessions() {
        activeUsers.clear();
        log.info("All active sessions cleared");
    }
}

