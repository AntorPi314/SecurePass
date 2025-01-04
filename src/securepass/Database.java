package securepass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private Connection connection;

    public void connect() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            System.out.println("Connection established.");
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void createUserTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS users ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "username TEXT NOT NULL,"
                + "password TEXT NOT NULL,"
                + "email TEXT NOT NULL"
                + ");";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            System.out.println("Error in creating users table: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean validateUser(String username, String password) {
        String selectSQL = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(selectSQL)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("Error in validating user: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean addUser(String username, String password, String email) {
        String insertSQL = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(insertSQL)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, email);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("Error in adding user: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public void createRecordsTable(String username) {
        if (username == null || username.isEmpty()) {
            System.out.println("Invalid username provided.");
            return;
        }
        String tableName = "table_" + username;
        String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName
                + "( id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "type TEXT NOT NULL,"
                + "username TEXT NOT NULL,"
                + "password TEXT NOT NULL,"
                + "note TEXT,"
                + "created_time TEXT,"
                + "last_updated_time TEXT"
                + ");";
        System.out.println("Table Name: " + tableName);
        System.out.println("SQL Query: " + createTableSQL);
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
            System.out.println("Table " + tableName + " created successfully (if not exists).");
        } catch (SQLException e) {
            System.out.println("Error in creating records table: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void createInfoTable() {
        String tableName = "Info";
        String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName
                + " (loggedInUser TEXT"
                + ");";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
            System.out.println("Table " + tableName + " created successfully (if not exists).");
        } catch (SQLException e) {
            System.out.println("Error in creating records table: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public int addRecord(String username, String type, String dbUsername, String password, String note, String createdTime, String lastUpdatedTime) {
        String tableName = "table_" + username;
        String insertSQL = "INSERT INTO " + tableName + " (type, username, password, note, created_time, last_updated_time) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, type);
            stmt.setString(2, dbUsername);
            stmt.setString(3, password);
            stmt.setString(4, note);
            stmt.setString(5, createdTime);
            stmt.setString(6, lastUpdatedTime);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error in adding record: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    public boolean updateRecord(String username, int id, String type, String dbUsername, String password, String note, String lastUpdatedTime) {
        String tableName = "table_" + username;
        String updateSQL = "UPDATE " + tableName + " SET type = ?, username = ?, password = ?, note = ?, last_updated_time = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(updateSQL)) {
            stmt.setString(1, type);
            stmt.setString(2, dbUsername);
            stmt.setString(3, password);
            stmt.setString(4, note);
            stmt.setString(5, lastUpdatedTime);
            stmt.setInt(6, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error in updating record: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteRecord(String username, int id) {
        String tableName = "table_" + username;
        String deleteSQL = "DELETE FROM " + tableName + " WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteSQL)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error in deleting record: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public List<Record> getAllRecordsForUser(String username) {
        List<Record> records = new ArrayList<>();
        String tableName = "table_" + username;
        String selectSQL = "SELECT * FROM " + tableName;
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(selectSQL)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String type = rs.getString("type");
                String dbUsername = rs.getString("username");
                String password = rs.getString("password");
                String note = rs.getString("note");
                String createdTime = rs.getString("created_time");
                String lastUpdatedTime = rs.getString("last_updated_time");

                records.add(new Record(id, type, dbUsername, password, note, lastUpdatedTime, createdTime));
            }
        } catch (SQLException e) {
            System.out.println("Error in retrieving records: " + e.getMessage());
            e.printStackTrace();
        }
        return records;
    }

    public List<Record> searchRecordsForUser(String username, String query) {
        List<Record> records = new ArrayList<>();
        String tableName = "table_" + username;
        String selectSQL = "SELECT * FROM " + tableName + " WHERE type LIKE ? OR username LIKE ? OR note LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(selectSQL)) {
            String searchQuery = "%" + query + "%";
            stmt.setString(1, searchQuery);
            stmt.setString(2, searchQuery);
            stmt.setString(3, searchQuery);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String type = rs.getString("type");
                    String dbUsername = rs.getString("username");
                    String password = rs.getString("password");
                    String note = rs.getString("note");
                    String createdTime = rs.getString("created_time");
                    String lastUpdatedTime = rs.getString("last_updated_time");

                    records.add(new Record(id, type, dbUsername, password, note, lastUpdatedTime, createdTime));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error in searching records: " + e.getMessage());
            e.printStackTrace();
        }
        return records;
    }

    public boolean isTableExist(String tableName) {
        String checkTableSQL = "SELECT name FROM sqlite_master WHERE type='table' AND name=?";
        try (PreparedStatement stmt = connection.prepareStatement(checkTableSQL)) {
            stmt.setString(1, tableName);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("Error in checking if table exists: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public Record getRecordById(String username, int id) {
        String tableName = "table_" + username;
        String selectSQL = "SELECT * FROM " + tableName + " WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(selectSQL)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String type = rs.getString("type");
                    String dbUsername = rs.getString("username");
                    String password = rs.getString("password");
                    String note = rs.getString("note");
                    String createdTime = rs.getString("created_time");
                    String lastUpdatedTime = rs.getString("last_updated_time");

                    return new Record(id, type, dbUsername, password, note, lastUpdatedTime, createdTime);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error in retrieving record by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

}
