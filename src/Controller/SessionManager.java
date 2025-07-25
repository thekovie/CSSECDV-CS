package Controller;
import Model.Session;
import Controller.Main;


public class SessionManager {
    // public for other classes to use
    public static final int ROLE_DISABLED      = 1;
    public static final int ROLE_CLIENT        = 2;
    public static final int ROLE_STAFF         = 3;
    public static final int ROLE_MANAGER       = 4;
    public static final int ROLE_ADMINISTRATOR = 5;
    public static final int ROLE_NONE          = -1;
    public static final int NO_SESSION         = -1;
    
    
    
    private static Session currentSession = null;
    
    private static Main main;
    
    public static void initialize(Main mainController) {
        main = mainController;
    }

    

    public static void createSession(int userId, String username, int role, long durationMillis){
        if (durationMillis == 0){
            durationMillis = 30 * 60 * 1000; // default: 30 mins
        }

        // Build in-memory session
        currentSession = new Session(userId, username, role, durationMillis);

        main.sqlite.addSession(currentSession);

    }

    
    
    public static void restoreLastSession() {
    Session latest = main.sqlite.getLatestSession();
    if (latest != null && !latest.isExpired()) {
        currentSession = latest;
        System.out.println("Auto-login: Session restored for user " + latest.getUsername());
    }
}


    public static Session getSession() {
        if (!isLoggedIn()) return null;
        return currentSession;
    }


    public static boolean isLoggedIn() {
        if (currentSession == null) return false;

        if (currentSession.isExpired()) {
            logout(); // Auto-logout if expired
            return false;
        }
        return true;
    }


    public static int getCurrentSessionId() {
        return isLoggedIn() ? currentSession.getSessionId() : NO_SESSION;
    }
    
    public static String getUsername() {
        return currentSession.getUsername();
    }


    public static void logout() {
        if (currentSession != null) {
            main.sqlite.removeAllSessionsForUser(currentSession.getUserId());
            currentSession = null;
        }
    }

    
    public static int getSessionRole() {
        return isLoggedIn() ? currentSession.getRole() : ROLE_NONE;
    }

    
    public static boolean isRole(int role) {
        return getSessionRole() == role;
    }


    
}
