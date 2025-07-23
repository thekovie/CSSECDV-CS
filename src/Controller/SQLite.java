package Controller;

import Model.History;
import Model.Logs;
import Model.Product;
import Model.User;
import Model.Session;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SQLite {
    
    public int DEBUG_MODE = 0;
    String driverURL = "jdbc:sqlite:" + "database.db";
    
    public void createNewDatabase() {
        try (Connection conn = DriverManager.getConnection(driverURL)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("Database database.db created.");
            }
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }
    
    public void createHistoryTable() {
        String sql = "CREATE TABLE IF NOT EXISTS history (\n"
            + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
            + " username TEXT NOT NULL,\n"
            + " name TEXT NOT NULL,\n"
            + " stock INTEGER DEFAULT 0,\n"
            + " timestamp TEXT NOT NULL\n"
            + ");";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table history in database.db created.");
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }
    
    public void createLogsTable() {
        String sql = "CREATE TABLE IF NOT EXISTS logs (\n"
            + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
            + " event TEXT NOT NULL,\n"
            + " username TEXT NOT NULL,\n"
            + " desc TEXT NOT NULL,\n"
            + " timestamp TEXT NOT NULL\n"
            + ");";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table logs in database.db created.");
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }
     
    public void createProductTable() {
        String sql = "CREATE TABLE IF NOT EXISTS product (\n"
            + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
            + " name TEXT NOT NULL UNIQUE,\n"
            + " stock INTEGER DEFAULT 0,\n"
            + " price REAL DEFAULT 0.00\n"
            + ");";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table product in database.db created.");
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }
     
    public void createUserTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (\n"
            + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
            + " username TEXT NOT NULL UNIQUE,\n"
            + " password TEXT NOT NULL,\n"
            + " role INTEGER DEFAULT 2,\n"
            + " locked INTEGER DEFAULT 0,\n"
            + " failed_attempts INTEGER DEFAULT 0,\n"
            + " last_failed_attempt TEXT\n"
            + ");";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table users in database.db created.");
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }

    public void dropHistoryTable() {
        String sql = "DROP TABLE IF EXISTS history;";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table history in database.db dropped.");
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }
    
    public void dropLogsTable() {
        String sql = "DROP TABLE IF EXISTS logs;";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table logs in database.db dropped.");
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }
    
    public void dropProductTable() {
        String sql = "DROP TABLE IF EXISTS product;";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table product in database.db dropped.");
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }
    
    public void dropUserTable() {
        String sql = "DROP TABLE IF EXISTS users;";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table users in database.db dropped.");
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }
    
    public void addHistory(String username, String name, int stock, String timestamp) {
        String sql = "INSERT INTO history(username,name,stock,timestamp) VALUES('" + username + "','" + name + "','" + stock + "','" + timestamp + "')";
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()){
            stmt.execute(sql);
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }
    
    public void addLogs(String event, String username, String desc, String timestamp) {
        String sql = "INSERT INTO logs(event,username,desc,timestamp) VALUES('" + event + "','" + username + "','" + desc + "','" + timestamp + "')";
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()){
            stmt.execute(sql);
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }
    
    public void addProduct(String name, int stock, double price) {
        String sql = "INSERT INTO product(name,stock,price) VALUES('" + name + "','" + stock + "','" + price + "')";
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()){
            stmt.execute(sql);
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }
    
    public void addUser(String username, String password) {
        String sql = "INSERT INTO users(username,password) VALUES('" + username + "','" + password + "')";
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()){
            stmt.execute(sql);
            
//      PREPARED STATEMENT EXAMPLE
//      String sql = "INSERT INTO users(username,password) VALUES(?,?)";
//      PreparedStatement pstmt = conn.prepareStatement(sql)) {
//      pstmt.setString(1, username);
//      pstmt.setString(2, password);
//      pstmt.executeUpdate();
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }
    
    
    public ArrayList<History> getHistory(){
        String sql = "SELECT id, username, name, stock, timestamp FROM history";
        ArrayList<History> histories = new ArrayList<History>();
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            
            while (rs.next()) {
                histories.add(new History(rs.getInt("id"),
                                   rs.getString("username"),
                                   rs.getString("name"),
                                   rs.getInt("stock"),
                                   rs.getString("timestamp")));
            }
        } catch (Exception ex) {
            System.out.print(ex);
        }
        return histories;
    }
    
    public ArrayList<Logs> getLogs(){
        String sql = "SELECT id, event, username, desc, timestamp FROM logs";
        ArrayList<Logs> logs = new ArrayList<Logs>();
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            
            while (rs.next()) {
                logs.add(new Logs(rs.getInt("id"),
                                   rs.getString("event"),
                                   rs.getString("username"),
                                   rs.getString("desc"),
                                   rs.getString("timestamp")));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return logs;
    }
    
    public ArrayList<Product> getProduct(){
        String sql = "SELECT id, name, stock, price FROM product";
        ArrayList<Product> products = new ArrayList<Product>();
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            
            while (rs.next()) {
                products.add(new Product(rs.getInt("id"),
                                   rs.getString("name"),
                                   rs.getInt("stock"),
                                   rs.getFloat("price")));
            }
        } catch (Exception ex) {
            System.out.print(ex);
        }
        return products;
    }
    
    public ArrayList<User> getUsers(){
        String sql = "SELECT id, username, password, role, locked, failed_attempts, last_failed_attempt FROM users";
        ArrayList<User> users = new ArrayList<User>();
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            
            while (rs.next()) {
                users.add(new User(rs.getInt("id"),
                                   rs.getString("username"),
                                   rs.getString("password"),
                                   rs.getInt("role"),
                                   rs.getInt("locked"),
                                   rs.getInt("failed_attempts"),
                                   rs.getString("last_failed_attempt")));
            }
        } catch (Exception ex) {}
        return users;
    }
    
    public User getUser(String username){
        String sql = "SELECT id, username, password, role, locked, failed_attempts, last_failed_attempt FROM users WHERE username = ? LIMIT 1";

        try (Connection conn = DriverManager.getConnection(driverURL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password"), //already hashed
                    rs.getInt("role"),
                    rs.getInt("locked"),
                    rs.getInt("failed_attempts"),
                    rs.getString("last_failed_attempt")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // means user not found
    }
    
    public void addUser(String username, String password, int role) {
        String sql = "INSERT INTO users(username,password,role) VALUES('" + username + "','" + password + "','" + role + "')";
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()){
            stmt.execute(sql);
            
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }
    
    public void removeUser(String username) {
        String sql = "DELETE FROM users WHERE username='" + username + "';";

        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("User " + username + " has been deleted.");
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }
    
    public Product getProduct(String name){
        String sql = "SELECT name, stock, price FROM product WHERE name='" + name + "';";
        Product product = null;
        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            product = new Product(rs.getString("name"),
                                   rs.getInt("stock"),
                                   rs.getFloat("price"));
        } catch (Exception ex) {
            System.out.print(ex);
        }
        return product;
    }
    
    public boolean usernameExists(String username) {
        String sql = "SELECT 1 FROM users WHERE username = ?";
         try (Connection conn = DriverManager.getConnection(driverURL);
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setString(1, username);
        ResultSet rs = pstmt.executeQuery();

        return rs.next();
        
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
    
    public void updateFailedLogin(String username, int failedAttempts, String timestamp) {
        String sql = "UPDATE users SET failed_attempts = ?, last_failed_attempt = ? WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(driverURL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, failedAttempts);
            pstmt.setString(2, timestamp);
            pstmt.setString(3, username);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }   
    
    public void resetLoginAttempts(String username) {
        String sql = "UPDATE users SET failed_attempts = 0, last_failed_attempt = NULL WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(driverURL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void lockUser(String username) {
        String sql = "UPDATE users SET locked = 1 WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(driverURL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // session table methods
    public void createSessionsTable() {
        String sql = "CREATE TABLE IF NOT EXISTS sessions (\n"
            + " session_id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
            + " user_id INTEGER NOT NULL,\n"
            + " username TEXT NOT NULL,\n"
            + " role INTEGER NOT NULL,\n"
            + " login_time TEXT NOT NULL,\n"
            + " expires_at TEXT NOT NULL,\n"
            + " FOREIGN KEY (user_id) REFERENCES users(id)\n"
            + ");";

        try (Connection conn = DriverManager.getConnection(driverURL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table sessions in database.db created.");
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }

    public void dropSessionsTable() {
        String sql = "DROP TABLE IF EXISTS sessions;";

        try (Connection conn = DriverManager.getConnection(driverURL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table sessions in database.db dropped.");
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }

    
    public void addSession(Session session) {
        String sql = "INSERT INTO sessions(user_id, username, role, login_time, expires_at) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(driverURL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, session.getUserId());
            pstmt.setString(2, session.getUsername());
            pstmt.setInt(3, session.getRole());
            pstmt.setString(4, session.getLoginTimestamp().toString());
            pstmt.setString(5, session.getExpiresAt().toString());

            pstmt.executeUpdate();
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }

    // deprecated, but kept for now just in case if needed.
    public void removeSession(int sessionId) {
        String sql = "DELETE FROM sessions WHERE session_id = ?";

        try (Connection conn = DriverManager.getConnection(driverURL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, sessionId);
            pstmt.executeUpdate();

        } catch (Exception ex) {
            System.out.print(ex);
        }
    }
    
    // effectively logs out all sessions for a user, so that the edge case of logging in in 2 windows and one logs out, all other windows will be logged out as well.
    // prevents residual sessions
    public void removeAllSessionsForUser(int userId) {
        String sql = "DELETE FROM sessions WHERE user_id = ?";
        try (Connection conn = DriverManager.getConnection(driverURL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }

    
    public void deleteExpiredSessions() {
        String sql = "DELETE FROM sessions WHERE expires_at <= datetime('now')";
        try (Connection conn = DriverManager.getConnection(driverURL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }

    public Session getLatestSession() {
        String sql = "SELECT * FROM sessions ORDER BY session_id DESC LIMIT 1";
        try (Connection conn = DriverManager.getConnection(driverURL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return new Session(
                    rs.getInt("session_id"),
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getInt("role"),
                    rs.getString("login_time"),
                    rs.getString("expires_at")
                );
            }

        } catch (Exception e) {
            System.out.println("Error restoring session: " + e.getMessage());
        }
        return null;
    }
        
    public ArrayList<Session> getSessions(int userId) {
        String sql = "SELECT session_id, user_id, username, role, login_time, expires_at FROM sessions WHERE user_id = ?";
        ArrayList<Session> sessions = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(driverURL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    sessions.add(new Session(
                        rs.getInt("session_id"),
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getInt("role"),
                        rs.getString("login_time"),
                        rs.getString("expires_at")
                    ));
                }
            }
        } catch (Exception ex) {
            System.out.print(ex);
        }

        return sessions;
    }





}