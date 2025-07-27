package Controller;

public class LoggerUtil {
    private final SQLite sqlite;

    public LoggerUtil(SQLite sqlite) {
        this.sqlite = sqlite;
    }

    public void debug(String message) {
        if (sqlite.DEBUG_MODE == 1) {
            System.out.println("DEBUG | " + message);
        }
    }

    public void logSqlParams(String prefix, Object... params) {
        if (sqlite.DEBUG_MODE == 1) {
            StringBuilder sb = new StringBuilder(prefix + ": ");
            for (int i = 0; i < params.length; i++) {
                sb.append("[").append(i).append("]=").append(params[i]).append(" ");
            }
            System.out.println("DEBUG | " + sb.toString().trim());
        }
    }
}
