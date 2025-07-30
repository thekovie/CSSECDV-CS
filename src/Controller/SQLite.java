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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class SQLite {    
    public int DEBUG_MODE = 0;
    String driverURL = "jdbc:sqlite:" + "database.db";
    
    // this is in charge of printing debug logs whenever admin
    // decides to use the debug mode.
    private final LoggerUtil logger;
    
    public SQLite() {
        this.logger = new LoggerUtil(this);
    }
    
    public LoggerUtil getLogger() {
        return logger;
    }
    
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
            + " price REAL DEFAULT 0.00,\n"
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
            + " price REAL DEFAULT 0.00,\n"
            + " deleted INTEGER DEFAULT 0\n"  // <-- Soft delete flag
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
    
    public void addHistory(String username, String name, int stock, double price, String timestamp) {
        String sql = "INSERT INTO history(username, name, price, stock, timestamp) VALUES (?, ?, ?, ?, ?)";

        logger.debug("Preparing to insert history record.");
        logger.logSqlParams("INSERT INTO history", username, name, price, stock, timestamp);

        try (Connection conn = DriverManager.getConnection(driverURL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, name);
            pstmt.setDouble(3, price);
            pstmt.setInt(4, stock);
            pstmt.setString(5, timestamp);

            int rows = pstmt.executeUpdate();
            logger.debug("History record inserted. Rows affected: " + rows);

        } catch (Exception ex) {
            System.out.print("Error adding history: " + ex.getMessage());
            logger.debug("Failed to insert history: " + ex.getMessage());
        }
    }


    public void addLogs(String event, String username, String desc, String timestamp) {
        String sql = "INSERT INTO logs(event, username, desc, timestamp) VALUES (?, ?, ?, ?)";

        logger.debug("Preparing to insert log.");
        logger.logSqlParams("INSERT INTO logs", event, username, desc, timestamp);

        try (Connection conn = DriverManager.getConnection(driverURL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, event);
            pstmt.setString(2, username);
            pstmt.setString(3, desc);
            pstmt.setString(4, timestamp);

            int rows = pstmt.executeUpdate();
            logger.debug("Log inserted. Rows affected: " + rows);

        } catch (Exception ex) {
            System.out.print("Error adding log: " + ex.getMessage());
            logger.debug("Failed to insert log: " + ex.getMessage());
        }
    }

public void addProduct(String name, int stock, double price, String performedBy) {
    String productSql = "INSERT INTO product(name, stock, price) VALUES (?, ?, ?)";
    String logSql = "INSERT INTO logs(event, username, desc, timestamp) VALUES (?, ?, ?, ?)";

    logger.debug("Starting transaction to add product: " + name);
    logger.logSqlParams("Product parameters", name, stock, price);

    try (Connection conn = DriverManager.getConnection(driverURL)) {
        conn.setAutoCommit(false);

        try (
            PreparedStatement productStmt = conn.prepareStatement(productSql);
            PreparedStatement logStmt = conn.prepareStatement(logSql)
        ) {
            productStmt.setString(1, name);
            productStmt.setInt(2, stock);
            productStmt.setDouble(3, price);
            int rows = productStmt.executeUpdate();

            logger.debug("Product insert executed. Rows affected: " + rows);

            String timestamp = new Timestamp(System.currentTimeMillis()).toString();
            logStmt.setString(1, "NOTICE");
            logStmt.setString(2, performedBy);
            logStmt.setString(3, "Product \"" + name + "\" added.");
            logStmt.setString(4, timestamp);
            int logRows = logStmt.executeUpdate();

            logger.debug("Log insert executed. Rows affected: " + logRows);

            conn.commit();
            logger.debug("Transaction committed successfully.");

        } catch (Exception ex) {
            conn.rollback();
            logger.debug("Transaction rolled back due to error: " + ex.getMessage());

            if (ex.getMessage().contains("UNIQUE constraint failed: product.name")) {
                throw new IllegalArgumentException("This product already exists. Please use the EDIT feature instead.");
            } else {
                throw new RuntimeException("Error adding product: " + ex.getMessage());
            }
        }

    } catch (Exception ex) {
        logger.debug("Connection or outer transaction failure: " + ex.getMessage());
        throw new RuntimeException("Transaction failed: " + ex.getMessage());
    }
}

    public void updateProduct(String originalName, String newName, int newStock, double newPrice, String performedBy) {
        String updateSql = "UPDATE product SET name = ?, stock = ?, price = ? WHERE name = ?";
        String logSql = "INSERT INTO logs(event, username, desc, timestamp) VALUES (?, ?, ?, ?)";

        logger.debug("Starting transaction to update product: " + originalName);
        logger.logSqlParams("Update parameters", newName, newStock, newPrice, originalName);

        try (Connection conn = DriverManager.getConnection(driverURL)) {
            conn.setAutoCommit(false);

            try (
                PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                PreparedStatement logStmt = conn.prepareStatement(logSql)
            ) {
                updateStmt.setString(1, newName);
                updateStmt.setInt(2, newStock);
                updateStmt.setDouble(3, newPrice);
                updateStmt.setString(4, originalName);

                int rowsAffected = updateStmt.executeUpdate();
                logger.debug("Product update executed. Rows affected: " + rowsAffected);

                if (rowsAffected == 0) {
                    conn.rollback();
                    logger.debug("Update failed. No rows affected. Rolling back transaction.");
                    throw new IllegalArgumentException("Product not found. Update failed.");
                }

                String timestamp = new Timestamp(System.currentTimeMillis()).toString();
                String logDescription = String.format(
                    "Product \"%s\" updated: name=\"%s\", stock=%d, price=%.2f",
                    originalName, newName, newStock, newPrice
                );

                logStmt.setString(1, "NOTICE");
                logStmt.setString(2, performedBy);
                logStmt.setString(3, logDescription);
                logStmt.setString(4, timestamp);
                int logRows = logStmt.executeUpdate();

                logger.debug("Log insert executed. Rows affected: " + logRows);

                conn.commit();
                logger.debug("Transaction committed successfully.");

            } catch (Exception ex) {
                conn.rollback();
                logger.debug("Transaction rolled back due to error: " + ex.getMessage());
                throw new RuntimeException("Error updating product: " + ex.getMessage());
            }

        } catch (Exception ex) {
            logger.debug("Connection or outer transaction failure: " + ex.getMessage());
            throw new RuntimeException("Transaction failed: " + ex.getMessage());
        }
    }



    public ArrayList<Product> getProduct() {
        String sql = "SELECT id, name, stock, price FROM product WHERE deleted = 0";
        ArrayList<Product> products = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(driverURL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                products.add(new Product(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("stock"),
                    rs.getFloat("price")
                ));
            }
        } catch (Exception ex) {
            System.out.print(ex);
        }
        return products;
    }

    
    public Product getProduct(String name, boolean includeDeleted) {
        String sql = includeDeleted
            ? "SELECT name, stock, price FROM product WHERE name = ?"
            : "SELECT name, stock, price FROM product WHERE name = ? AND deleted = 0";

        Product product = null;

        try (Connection conn = DriverManager.getConnection(driverURL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                product = new Product(
                    rs.getString("name"),
                    rs.getInt("stock"),
                    rs.getFloat("price")
                );
            }

        } catch (Exception ex) {
            System.out.print(ex);
        }
        return product;
    }

    public boolean purchaseProduct(String name, int quantity, String username) {
        String selectSql = "SELECT stock, price FROM product WHERE name = ?";
        String updateSql = "UPDATE product SET stock = stock - ? WHERE name = ?";
        String historySql = "INSERT INTO history(username, name, price, stock, timestamp) VALUES (?, ?, ?, ?, ?)";

        logger.debug("Starting transaction to purchase product: " + name);
        logger.logSqlParams("Input", name, quantity, username);

        try (Connection conn = DriverManager.getConnection(driverURL)) {
            conn.setAutoCommit(false); // Start transaction

            try (
                PreparedStatement selectStmt = conn.prepareStatement(selectSql);
                PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                PreparedStatement historyStmt = conn.prepareStatement(historySql)
            ) {
                // Fetch stock
                selectStmt.setString(1, name);
                ResultSet rs = selectStmt.executeQuery();

                if (!rs.next()) {
                    logger.debug("Product not found.");
                    return false;
                }

                int stock = rs.getInt("stock");
                logger.debug("Current stock: " + stock + ", Requested quantity: " + quantity);

                if (stock < quantity) {
                    logger.debug("Insufficient stock. Aborting transaction.");
                    return false;
                }
                
                double price = rs.getDouble("price"); //current price
                logger.debug("Current price: " + price);

                // Update stock
                updateStmt.setInt(1, quantity);
                updateStmt.setString(2, name);
                int rowsAffected = updateStmt.executeUpdate();
                logger.debug("Product stock updated. Rows affected: " + rowsAffected);

                if (rowsAffected <= 0) {
                    logger.debug("Stock update failed. Aborting transaction.");
                    return false;
                }

                // Insert history
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
                historyStmt.setString(1, username);
                historyStmt.setString(2, name);
                historyStmt.setDouble(3, price);
                historyStmt.setInt(4, quantity);
                historyStmt.setString(5, timestamp);
                historyStmt.executeUpdate();

                logger.debug("Purchase history recorded for " + username + " at " + timestamp);

                // Commit the transaction
                conn.commit();
                logger.debug("Transaction committed successfully.");
                return true;

            } catch (Exception innerEx) {
                conn.rollback();
                logger.debug("Transaction rolled back due to: " + innerEx.getMessage());
                return false;
            }

        } catch (Exception ex) {
            logger.debug("Connection error during purchase: " + ex.getMessage());
            return false;
        }
    }

    public void deleteProduct(String name, String performedBy) {
        String softDeleteSql = "UPDATE product SET deleted = 1 WHERE name = ?";
        String logSql = "INSERT INTO logs(event, username, desc, timestamp) VALUES (?, ?, ?, ?)";

        logger.debug("Starting transaction to soft delete product: " + name);
        logger.logSqlParams("Delete Params", name, performedBy);

        try (Connection conn = DriverManager.getConnection(driverURL)) {
            conn.setAutoCommit(false);

            try (
                PreparedStatement deleteStmt = conn.prepareStatement(softDeleteSql);
                PreparedStatement logStmt = conn.prepareStatement(logSql)
            ) {
                deleteStmt.setString(1, name);
                int rowsAffected = deleteStmt.executeUpdate();
                logger.debug("Soft delete executed. Rows affected: " + rowsAffected);

                if (rowsAffected == 0) {
                    conn.rollback();
                    logger.debug("No product found to delete. Rolling back.");
                    throw new IllegalArgumentException("Product not found. Deletion failed.");
                }

                String timestamp = new Timestamp(System.currentTimeMillis()).toString();
                String logMessage = "Product \"" + name + "\" marked as deleted.";

                logStmt.setString(1, "NOTICE");
                logStmt.setString(2, performedBy);
                logStmt.setString(3, logMessage);
                logStmt.setString(4, timestamp);
                logStmt.executeUpdate();

                logger.debug("Log entry inserted: " + logMessage);

                conn.commit();
                logger.debug("Transaction committed successfully.");
            } catch (Exception ex) {
                conn.rollback();
                logger.debug("Transaction rolled back due to error: " + ex.getMessage());
                throw new RuntimeException("Error during soft deletion: " + ex.getMessage());
            }

        } catch (Exception ex) {
            logger.debug("Connection or outer transaction failure: " + ex.getMessage());
            throw new RuntimeException("Transaction failed: " + ex.getMessage());
        }
    }


    public void addUser(String username, String password) {
        String sql = "INSERT INTO users(username, password) VALUES (?, ?)";
        String logSql = "INSERT INTO logs(event, username, desc, timestamp) VALUES (?, ?, ?, ?)";
        String timestamp = new Timestamp(System.currentTimeMillis()).toString();

        logger.debug("Preparing to insert user: " + username);
        logger.logSqlParams("Insert User Params", username, "[PROTECTED]");

        try (Connection conn = DriverManager.getConnection(driverURL)) {
            conn.setAutoCommit(false);

            try (
                PreparedStatement pstmt = conn.prepareStatement(sql);
                PreparedStatement logStmt = conn.prepareStatement(logSql)
            ) {
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                int rows = pstmt.executeUpdate();

                logger.debug("User insert executed. Rows affected: " + rows);

                logStmt.setString(1, "NOTICE");
                logStmt.setString(2, username);
                logStmt.setString(3, "User registered successfully.");
                logStmt.setString(4, timestamp);
                logStmt.executeUpdate();

                conn.commit();
            } catch (Exception ex) {
                conn.rollback();

                logger.debug("Failed to insert user: " + ex.getMessage());
                System.out.print("Error adding user: " + ex.getMessage());

                try (PreparedStatement logStmt = conn.prepareStatement(logSql)) {
                    logStmt.setString(1, "ALERT");
                    logStmt.setString(2, username);
                    logStmt.setString(3, "Failed to register user: " + ex.getMessage());
                    logStmt.setString(4, timestamp);
                    logStmt.executeUpdate();
                } catch (Exception logEx) {
                    logger.debug("Failed to log registration error: " + logEx.getMessage());
                }
            }
        } catch (Exception ex) {
            logger.debug("Outer transaction failure: " + ex.getMessage());
            System.out.print("Error adding user: " + ex.getMessage());
        }
    }


    public ArrayList<History> getHistory(){
        String sql = "SELECT id, username, name, stock, price, timestamp FROM history";
        ArrayList<History> histories = new ArrayList<History>();
        
        try (Connection conn = DriverManager.getConnection(driverURL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            
            while (rs.next()) {
                histories.add(new History(rs.getInt("id"),
                                   rs.getString("username"),
                                   rs.getString("name"),
                                   rs.getInt("stock"),
                                   rs.getDouble("price"),
                                   rs.getString("timestamp")));
            }
        } catch (Exception ex) {
            System.out.print(ex);
        }
        return histories;
    }
    
    public ArrayList<History> getHistoryWithUsername(String username) {
        String sql = "SELECT id, username, name, stock, price, timestamp FROM history WHERE username = ?";
        ArrayList<History> histories = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(driverURL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                histories.add(new History(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("name"),
                    rs.getInt("stock"),
                    rs.getDouble("price"),
                    rs.getString("timestamp")
                ));
            }

        } catch (Exception ex) {
            System.out.print("Error fetching user history: " + ex.getMessage());
        }

        return histories;
    }
    
    public ArrayList<History> searchHistory(String query) {
        String sql = "SELECT id, username, name, stock, price, timestamp FROM history WHERE username LIKE ? OR name LIKE ?";

        ArrayList<History> histories = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(driverURL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String pattern = "%" + query + "%";
            pstmt.setString(1, pattern);
            pstmt.setString(2, pattern);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                histories.add(new History(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("name"),
                    rs.getInt("stock"),
                    rs.getDouble("price"),
                    rs.getString("timestamp")
                ));
            }
        } catch (Exception ex) {
            System.out.println("Error searching history: " + ex.getMessage());
        }

        return histories;
    }
    
    public ArrayList<History> searchHistoryForUser(String username, String query) {
        String sql = "SELECT id, username, name, stock, price, timestamp FROM history WHERE username = ? AND name LIKE ?";

        ArrayList<History> histories = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(driverURL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, "%" + query + "%");

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                histories.add(new History(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("name"),
                    rs.getInt("stock"),
                    rs.getDouble("price"),
                    rs.getString("timestamp")
                ));
            }
        } catch (Exception ex) {
            System.out.println("Error searching user history: " + ex.getMessage());
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
        String sql = "INSERT INTO users(username, password, role) VALUES (?, ?, ?)";

        logger.debug("Preparing to insert user: " + username);
        logger.logSqlParams("Insert User Params", username, "[PROTECTED]", role);

        try (Connection conn = DriverManager.getConnection(driverURL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setInt(3, role);
            int rows = pstmt.executeUpdate();

            logger.debug("User insert executed. Rows affected: " + rows);

        } catch (Exception ex) {
            if (ex.getMessage().contains("UNIQUE constraint failed: users.username")) {
                logger.debug("Insert failed: Username already exists.");
                System.out.print("Username already exists.");
            } else {
                logger.debug("Insert failed: " + ex.getMessage());
                System.out.print("Error adding user: " + ex.getMessage());
            }
        }
    }

    public void updatePassword(String username, String newPassword) {
        String sqlUpdate = "UPDATE users SET password = ? WHERE username = ?";
        String sqlLog = "INSERT INTO logs(event, username, desc, timestamp) VALUES (?, ?, ?, ?)";

        logger.debug("Starting password update transaction for user: " + username);

        try (Connection conn = DriverManager.getConnection(driverURL)) {
            conn.setAutoCommit(false); // Begin transaction

            try (
                PreparedStatement updateStmt = conn.prepareStatement(sqlUpdate);
                PreparedStatement logStmt = conn.prepareStatement(sqlLog)
            ) {
                // Hash the password before saving
                String hashedPassword = User.hashPassword(newPassword);

                // Update user password
                updateStmt.setString(1, hashedPassword);
                updateStmt.setString(2, username);
                logger.logSqlParams("Update Password Params", "[HASHED_PASSWORD]", username);
                int rowsAffected = updateStmt.executeUpdate();

                if (rowsAffected == 0) {
                    conn.rollback();
                    logger.debug("No rows affected. Password update failed for user: " + username);
                    throw new SQLException("User not found. Password update failed.");
                }

                // Log password change
                String timestamp = new Timestamp(System.currentTimeMillis()).toString();
                logStmt.setString(1, "NOTICE");
                logStmt.setString(2, username);
                logStmt.setString(3, "Password was updated.");
                logStmt.setString(4, timestamp);
                logStmt.executeUpdate();

                conn.commit();
                logger.debug("Password update and log committed for user: " + username);

            } catch (Exception ex) {
                conn.rollback();
                logger.debug("Transaction rolled back due to: " + ex.getMessage());
                throw new RuntimeException("Error updating password: " + ex.getMessage());
            }

        } catch (SQLException e) {
            logger.debug("Password update transaction failed: " + e.getMessage());
            throw new RuntimeException("Transaction failed: " + e.getMessage());
        }
    }
 
    public void updateUserRole(String username, int newRole, String performedBy) {
        String sql;
        String logSql = "INSERT INTO logs(event, username, desc, timestamp) VALUES (?, ?, ?, ?)";
        String timestamp = new Timestamp(System.currentTimeMillis()).toString();

        boolean isDisable = (newRole == SessionManager.ROLE_DISABLED);
        sql = isDisable
            ? "UPDATE users SET role = 1, locked = 1 WHERE username = ?"
            : "UPDATE users SET role = ?, locked = 0 WHERE username = ?";

        logger.debug("Preparing to update role for user: " + username);
        logger.debug("SQL: " + sql);

        try (Connection conn = DriverManager.getConnection(driverURL)) {
            conn.setAutoCommit(false);

            try (
                PreparedStatement roleStmt = conn.prepareStatement(sql);
                PreparedStatement logStmt = conn.prepareStatement(logSql)
            ) {
                if (isDisable) {
                    roleStmt.setString(1, username);
                    logger.logSqlParams("Role Update Params", username);
                } else {
                    roleStmt.setInt(1, newRole);
                    roleStmt.setString(2, username);
                    logger.logSqlParams("Role Update Params", newRole, username);
                }

                int updated = roleStmt.executeUpdate();
                logger.debug("Rows affected by role update: " + updated);

                if (updated == 0) {
                    conn.rollback();
                    logger.debug("No user found. Rolling back.");
                    throw new SQLException("No user found with username: " + username);
                }

                String roleLabel;
                switch (newRole) {
                    case SessionManager.ROLE_ADMINISTRATOR:
                        roleLabel = "ADMIN";
                        break;
                    case SessionManager.ROLE_DISABLED:
                        roleLabel = "DISABLED";
                        break;
                    case SessionManager.ROLE_CLIENT:
                        roleLabel = "CLIENT";
                        break;
                    case SessionManager.ROLE_STAFF:
                        roleLabel = "STAFF";
                        break;
                    case SessionManager.ROLE_MANAGER:
                        roleLabel = "MANAGER";
                        break;
                    default:
                        roleLabel = "UNKNOWN(" + newRole + ")";
                        break;
                }

                logStmt.setString(1, "ALERT");
                logStmt.setString(2, performedBy);
                logStmt.setString(3, "Updated role of '" + username + "' to " + roleLabel + ".");
                logStmt.setString(4, timestamp);
                logger.logSqlParams("Log Params", "ALERT", performedBy, "Updated role of '" + username + "' to " + roleLabel + ".", timestamp);
                logStmt.executeUpdate();

                conn.commit();
                logger.debug("Transaction committed. Role update complete for " + username);

            } catch (Exception ex) {
                conn.rollback();
                logger.debug("Transaction rolled back due to: " + ex.getMessage());
                throw new RuntimeException("Failed to update user role: " + ex.getMessage());
            }

        } catch (SQLException e) {
            logger.debug("Database connection error: " + e.getMessage());
            throw new RuntimeException("Database error: " + e.getMessage());
        }
    }

    public void removeUser(String targetUsername, String performedBy) {
        String sqlDelete = "DELETE FROM users WHERE username = ?";
        String sqlLog = "INSERT INTO logs(event, username, desc, timestamp) VALUES (?, ?, ?, ?)";
        String timestamp = new Timestamp(System.currentTimeMillis()).toString();

        logger.debug("Preparing to delete user: " + targetUsername);
        logger.debug("SQL (Delete): " + sqlDelete);
        logger.debug("SQL (Log): " + sqlLog);

        try (Connection conn = DriverManager.getConnection(driverURL)) {
            conn.setAutoCommit(false);

            try (
                PreparedStatement deleteStmt = conn.prepareStatement(sqlDelete);
                PreparedStatement logStmt = conn.prepareStatement(sqlLog)
            ) {
                deleteStmt.setString(1, targetUsername);
                logger.logSqlParams("Delete Params", targetUsername);

                int rows = deleteStmt.executeUpdate();
                logger.debug("Rows affected by deletion: " + rows);

                if (rows == 0) {
                    conn.rollback();
                    logger.debug("User not found. Rolling back.");
                    throw new IllegalArgumentException("User not found. Deletion failed.");
                }

                logStmt.setString(1, "CRITICAL");
                logStmt.setString(2, performedBy);
                logStmt.setString(3, "Deleted user: " + targetUsername);
                logStmt.setString(4, timestamp);
                logger.logSqlParams("Log Params", "CRITICAL", performedBy, "Deleted user: " + targetUsername, timestamp);

                logStmt.executeUpdate();
                conn.commit();

                logger.debug("User deletion committed successfully.");
                System.out.println("User " + targetUsername + " has been deleted.");

            } catch (Exception innerEx) {
                conn.rollback();
                logger.debug("Transaction rolled back due to: " + innerEx.getMessage());
                throw new RuntimeException("Transaction failed: " + innerEx.getMessage());
            }

        } catch (Exception ex) {
            logger.debug("Error during user deletion: " + ex.getMessage());
            System.out.print("Error removing user: " + ex.getMessage());
        }
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
    
    public void lockUser(String username, String performedBy) {
        String sqlLock = "UPDATE users SET locked = 1 WHERE username = ?";
        String sqlLog = "INSERT INTO logs(event, username, desc, timestamp) VALUES (?, ?, ?, ?)";
        String timestamp = new Timestamp(System.currentTimeMillis()).toString();

        logger.debug("Preparing to lock user: " + username);
        logger.debug("SQL (Lock): " + sqlLock);
        logger.debug("SQL (Log): " + sqlLog);

        try (Connection conn = DriverManager.getConnection(driverURL)) {
            conn.setAutoCommit(false); // Begin transaction

            try (
                PreparedStatement lockStmt = conn.prepareStatement(sqlLock);
                PreparedStatement logStmt = conn.prepareStatement(sqlLog)
            ) {
                lockStmt.setString(1, username);
                logger.logSqlParams("Lock Params", username);

                int rowsAffected = lockStmt.executeUpdate();
                logger.debug("Rows affected by lock: " + rowsAffected);

                if (rowsAffected == 0) {
                    conn.rollback();
                    logger.debug("User not found or already locked. Rolling back.");
                    throw new SQLException("User not found or already locked.");
                }

                logStmt.setString(1, "ALERT");
                logStmt.setString(2, performedBy);
                logStmt.setString(3, "User '" + username + "' account was locked.");
                logStmt.setString(4, timestamp);
                logger.logSqlParams("Log Params", "ALERT", performedBy, "User '" + username + "' account was locked.", timestamp);

                logStmt.executeUpdate();

                conn.commit();
                logger.debug("Lock user transaction committed successfully.");

            } catch (Exception innerEx) {
                conn.rollback();
                logger.debug("Transaction rolled back due to: " + innerEx.getMessage());
            }

        } catch (SQLException e) {
            logger.debug("Error locking user: " + e.getMessage());
            System.out.println("Error locking user: " + e.getMessage());
        }
    }

    public void lockUserWithReason(String username, String performedBy, String reason) {
        String sqlLock = "UPDATE users SET locked = 1 WHERE username = ?";
        String sqlLog = "INSERT INTO logs(event, username, desc, timestamp) VALUES (?, ?, ?, ?)";
        String timestamp = new Timestamp(System.currentTimeMillis()).toString();

        logger.debug("Preparing to lock user with reason: " + username);
        logger.debug("SQL (Lock): " + sqlLock);
        logger.debug("SQL (Log): " + sqlLog);

        try (Connection conn = DriverManager.getConnection(driverURL)) {
            conn.setAutoCommit(false);

            try (
                PreparedStatement lockStmt = conn.prepareStatement(sqlLock);
                PreparedStatement logStmt = conn.prepareStatement(sqlLog)
            ) {
                lockStmt.setString(1, username);
                logger.logSqlParams("Lock Params", username);

                int lockRows = lockStmt.executeUpdate();
                logger.debug("Rows affected by lock: " + lockRows);

                if (lockRows == 0) {
                    conn.rollback();
                    logger.debug("User not found or already locked. Rolling back.");
                    throw new SQLException("User not found or already locked.");
                }

                String logMessage = "User " + username + " was locked by " + performedBy + ". REASON: " + reason;
                logStmt.setString(1, "CRITICAL");
                logStmt.setString(2, performedBy);
                logStmt.setString(3, logMessage);
                logStmt.setString(4, timestamp);
                logger.logSqlParams("Log Params", "CRITICAL", performedBy, logMessage, timestamp);

                logStmt.executeUpdate();

                conn.commit();
                logger.debug("Lock with reason transaction committed successfully.");

            } catch (Exception ex) {
                conn.rollback();
                logger.debug("Transaction rolled back due to: " + ex.getMessage());
                throw new RuntimeException("Lock operation failed: " + ex.getMessage());
            }

        } catch (SQLException e) {
            logger.debug("Transaction failed: " + e.getMessage());
            throw new RuntimeException("Transaction failed: " + e.getMessage());
        }
    }

    public void unlockUser(String username, String performedBy) {
        String sqlUnlock = "UPDATE users SET locked = 0 WHERE username = ?";
        String sqlLog = "INSERT INTO logs(event, username, desc, timestamp) VALUES (?, ?, ?, ?)";
        String timestamp = new Timestamp(System.currentTimeMillis()).toString();

        logger.debug("Preparing to unlock user: " + username);
        logger.debug("SQL (Unlock): " + sqlUnlock);
        logger.debug("SQL (Log): " + sqlLog);

        try (Connection conn = DriverManager.getConnection(driverURL)) {
            conn.setAutoCommit(false); // Begin transaction

            try (
                PreparedStatement unlockStmt = conn.prepareStatement(sqlUnlock);
                PreparedStatement logStmt = conn.prepareStatement(sqlLog)
            ) {
                unlockStmt.setString(1, username);
                logger.logSqlParams("Unlock Params", username);

                int rowsAffected = unlockStmt.executeUpdate();
                logger.debug("Rows affected by unlock: " + rowsAffected);

                if (rowsAffected == 0) {
                    conn.rollback();
                    logger.debug("User not found or already unlocked. Rolling back.");
                    throw new SQLException("User not found or already unlocked.");
                }

                String message = "User '" + username + "' account was unlocked.";
                logStmt.setString(1, "CRITICAL");
                logStmt.setString(2, performedBy);
                logStmt.setString(3, message);
                logStmt.setString(4, timestamp);
                logger.logSqlParams("Log Params", "CRITICAL", performedBy, message, timestamp);

                logStmt.executeUpdate();

                conn.commit();
                logger.debug("Unlock transaction committed successfully.");

            } catch (Exception innerEx) {
                conn.rollback();
                logger.debug("Transaction rolled back: " + innerEx.getMessage());
                throw new RuntimeException("Unlock operation failed: " + innerEx.getMessage());
            }

        } catch (SQLException e) {
            logger.debug("Error unlocking user: " + e.getMessage());
            throw new RuntimeException("Transaction failed: " + e.getMessage());
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
    
    // effectively logs out all sessions for a user, so that the edge case of logging in from 2 windows and one logs out, all other windows will be logged out as well.
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

    public void clearLogByDetails(String event, String username, String timestamp) {
        String sql = "DELETE FROM logs WHERE event = ? AND username = ? AND timestamp = ?";

        logger.debug("Preparing to clear log with specific details.");
        logger.debug("SQL: " + sql);
        logger.logSqlParams("Clear Log Params", event, username, timestamp);

        try (Connection conn = DriverManager.getConnection(driverURL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, event);
            stmt.setString(2, username);
            stmt.setString(3, timestamp);

            int rows = stmt.executeUpdate();
            logger.debug("Log entries deleted: " + rows);

        } catch (SQLException ex) {
            logger.debug("Error deleting log: " + ex.getMessage());
            System.out.println("Error deleting log: " + ex.getMessage());
        }
    }


}