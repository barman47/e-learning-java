package models;

import javafx.scene.control.Alert;
import sample.CustomAlert;

import java.sql.*;

public class AdminDataStore {
    public static final String DB_NAME = "elearning.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:C:\\databases\\" + DB_NAME;

    // Admin Database
    public static final String TABLE_ADMIN = "admins";
    public static final String COLUMN_ADMIN_ID = "id";
    public static final String COLUMN_ADMIN_NAME = "name";
    public static final String COLUMN_ADMIN_USERNAME = "username";
    public static final String COLUMN_ADMIN_PASSWORD = "password";
    public static final String CREATE_ADMIN_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_ADMIN  +
            " (" + COLUMN_ADMIN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_ADMIN_NAME + " TEXT NOT NULL, " +
            COLUMN_ADMIN_USERNAME + " TEXT NOT NULL, " +
            COLUMN_ADMIN_PASSWORD + " TEXT NOT NULL);";
    // Create admin
    public static final String INSERT_ADMIN = "INSERT INTO " + TABLE_ADMIN + "(" + COLUMN_ADMIN_NAME + ", " +
            COLUMN_ADMIN_USERNAME + "," + COLUMN_ADMIN_PASSWORD + ") VALUES(?, ?, ?)";
    private static final String FIND_ADMIN = "SELECT *  FROM " + TABLE_ADMIN + " WHERE " + COLUMN_ADMIN_USERNAME + " = ?;";

    // TEACHER DATABASE
//    public static final String TABLE_TEACHER = "teachers";
//    public static final String

    private Connection conn;
    private Statement statement;
    private ResultSet resultSet;

    private PreparedStatement insertAdmin;
    private PreparedStatement findAdmin;

    public boolean open () {
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING);
            // creating the admin table
            createAdminTable(); // Must come before insert, otherwise there may be no table to insert into
            findAdmin = conn.prepareStatement(FIND_ADMIN);
            insertAdmin = conn.prepareStatement(INSERT_ADMIN);
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            CustomAlert.showAlert(Alert.AlertType.ERROR, "Database Connection Error", "UNABLE TO CONNECT TO DATABASE!", "Please make sure your database is accessible and try again");
            return false;
        }
    }

    public void close (Connection conn, PreparedStatement preparedStatement, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }

            if (preparedStatement != null) {
                preparedStatement.close();
            }

            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            CustomAlert.showAlert(Alert.AlertType.WARNING, "Error Closing Database Connection", "UNABLE TO CLOSE DATABASE CONNECTION!", "Please retry.");
        }
    }

    public void close (Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }

            if (stmt != null) {
                stmt.close();
            }

            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            CustomAlert.showAlert(Alert.AlertType.WARNING, "Error Closing Database Connection", "UNABLE TO CLOSE DATABASE CONNECTION!", "Please retry.");
        }
    }

    public void close (Connection conn, Statement stmt) {
        try {
            if (stmt != null) {
                stmt.close();
            }

            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            CustomAlert.showAlert(Alert.AlertType.WARNING, "Error Closing Database Connection", "UNABLE TO CLOSE DATABASE CONNECTION!", "Please retry.");
        }
    }

    public void close (Connection conn, PreparedStatement preparedStatement) {
        try {
            if (preparedStatement != null) {
                preparedStatement.close();
            }

            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            CustomAlert.showAlert(Alert.AlertType.WARNING, "Error Closing Database Connection", "UNABLE TO CLOSE DATABASE CONNECTION!", "Please retry.");
        }
    }

    private void createAdminTable () {
        try {
            statement = conn.createStatement();
            statement.execute(CREATE_ADMIN_TABLE);
        } catch (SQLException ex) {
            CustomAlert.showAlert(Alert.AlertType.ERROR, "Error", "ERROR CREATING ADMIN TABLE", "Please try again!");
        }
    }

    public String insertAdmin (String name, String username, String password){
        String returnMessage = "";
        try {
            boolean adminExists = findAdmin(username);
            if (adminExists) {
                returnMessage = "Username exists!";
            } else {
                open();
                insertAdmin.setString(1, name);
                insertAdmin.setString(2, username);
                insertAdmin.setString(3, password);
                insertAdmin.execute();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            CustomAlert.showAlert(Alert.AlertType.ERROR, "Error", "ERROR INSERTING ADMIN", "Please try again!");
        }

        return returnMessage;
    }

    public boolean findAdmin (String username) {
        boolean admin = false;
        ResultSet resultSet = null;
        try {
            findAdmin.setString(1, username);
            resultSet = findAdmin.executeQuery();
            if (resultSet.next()) {
                admin = true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            CustomAlert.showAlert(Alert.AlertType.ERROR, "Error", "ERROR EXECUTING QUERY", "Please try again!");
        } finally {
            close(conn, findAdmin, resultSet);
        }
        return admin;
    }

    public Admin loginAdmin (String username, String password) {
        Admin admin = null;

        try {
            findAdmin.setString(1, username);
            resultSet = findAdmin.executeQuery();
            if (resultSet.next()) {
                admin = new Admin(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("username"), resultSet.getString("password"));
                if (!password.equals(admin.getPassword())) {
                    CustomAlert.showAlert(Alert.AlertType.WARNING, "Password Incorrect", "INCORRECT ADMIN PASSWORD!", "The Password provided is incorrect");
                }
            } else {
                CustomAlert.showAlert(Alert.AlertType.WARNING, "Admin not Found", "ADMIN DOES NOT EXIST!", "Admin username does not exist");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            close(conn, statement, resultSet);
        }
        return admin;
    }
}
