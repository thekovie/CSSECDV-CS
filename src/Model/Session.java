package Model;

import java.sql.Timestamp;

public class Session {
    private final int sessionId;
    private final int userId;
    private final String username;
    private final int role;
    private final Timestamp loginTimestamp;
    private final Timestamp expiresAt;

    public Session(int sessionId, int userId, String username, int role, String loginTime, String expiresAt) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.username = username;
        this.role = role;
        Timestamp loginTs = null;
        Timestamp expiryTs = null;
        try {
            loginTs = Timestamp.valueOf(loginTime);
            expiryTs = Timestamp.valueOf(expiresAt);
        } catch (IllegalArgumentException  e) {
            e.printStackTrace();
        }
        this.loginTimestamp = loginTs;
        this.expiresAt = expiryTs;
    }
    
    public Session(int userId, String username, int role, long durationMillis) {
        this.sessionId = -1; // will be assigned by DB
        this.userId = userId;
        this.username = username;
        this.role = role;
        this.loginTimestamp = new Timestamp(System.currentTimeMillis());
        this.expiresAt = new Timestamp(System.currentTimeMillis() + durationMillis);
    }

    public int getSessionId() {
        return sessionId;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public int getRole() {
        return role;
    }

    public Timestamp getLoginTimestamp() {
        return loginTimestamp;
    }

    public Timestamp getExpiresAt() {
        return expiresAt;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expiresAt.getTime();
    }
}
