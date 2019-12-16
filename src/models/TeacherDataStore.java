package models;

import javafx.scene.control.Alert;
import sample.CustomAlert;

import java.sql.*;

public class TeacherDataStore {
    public static final String DB_NAME = "elearning.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:C:\\databases\\" + DB_NAME;

    // Teacher Database
    public static final String TABLE_TEACHER = "teachers";
    public static final String COLUMN_TEACHER_ID = "id";
    public static final String COLUMN_COURSE_ID = "course_id";
    public static final String COLUMN_TEACHER_NAME = "name";
    public static final String COLUMN_TEACHER_USERNAME = "username";
    public static final String COLUMN_TEACHER_PASSWORD = "password";
    public static final String CREATE_TEACHER_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_TEACHER  +
            " (" + COLUMN_TEACHER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_COURSE_ID + " INTEGER, " +
            COLUMN_TEACHER_NAME + " TEXT NOT NULL, " +
            COLUMN_TEACHER_USERNAME + " TEXT NOT NULL, " +
            COLUMN_TEACHER_PASSWORD + " TEXT NOT NULL);";
    // Create teacher
    public static final String INSERT_TEACHER = "INSERT INTO " + TABLE_TEACHER + "(" + COLUMN_TEACHER_NAME + ", " +
            COLUMN_TEACHER_USERNAME + "," + COLUMN_TEACHER_PASSWORD + ") VALUES(?, ?, ?)";
    private static final String FIND_TEACHER = "SELECT *  FROM " + TABLE_TEACHER + " WHERE " + COLUMN_TEACHER_USERNAME + " = ?;";

    // TEACHER DATABASE
//    public static final String TABLE_TEACHER = "teachers";
//    public static final String

    private Connection conn;
    private Statement statement;
    private ResultSet resultSet;

    private PreparedStatement insertTeacher;
    private PreparedStatement findTeacher;

    public boolean open () {
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING);
            // creating the teacher table
            createTeacherTable(); // Must come before insert, otherwise there may be no table to insert into
            findTeacher = conn.prepareStatement(FIND_TEACHER);
            insertTeacher = conn.prepareStatement(INSERT_TEACHER);
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

    private void createTeacherTable () {
        try {
            statement = conn.createStatement();
            statement.execute(CREATE_TEACHER_TABLE);
        } catch (SQLException ex) {
            CustomAlert.showAlert(Alert.AlertType.ERROR, "Error", "ERROR CREATING TEACHER TABLE", "Please try again!");
        }
    }

    public String insertTeacher (String name, String username, String password){
        String returnMessage = "";
        try {
            boolean teacherExists = findTeacher(username);
            if (teacherExists) {
                returnMessage = "Username exists!";
            } else {
                open();
                insertTeacher.setString(1, name);
                insertTeacher.setString(2, username);
                insertTeacher.setString(3, password);
                insertTeacher.execute();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            CustomAlert.showAlert(Alert.AlertType.ERROR, "Error", "ERROR INSERTING TEACHER", "Please try again!");
        }

        return returnMessage;
    }

    public boolean findTeacher (String username) {
        boolean teacher = false;
        ResultSet resultSet = null;
        try {
            findTeacher.setString(1, username);
            resultSet = findTeacher.executeQuery();
            if (resultSet.next()) {
                teacher = true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            CustomAlert.showAlert(Alert.AlertType.ERROR, "Error", "ERROR EXECUTING QUERY", "Please try again!");
        } finally {
            close(conn, findTeacher, resultSet);
        }
        return teacher;
    }

    public Teacher loginTeacher (String username, String password) {
        Teacher teacher = null;

        try {
            findTeacher.setString(1, username);
            resultSet = findTeacher.executeQuery();
            if (resultSet.next()) {
                teacher = new Teacher(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("username"), resultSet.getString("password"));
                if (!password.equals(teacher.getPassword())) {
                    CustomAlert.showAlert(Alert.AlertType.WARNING, "Password Incorrect", "INCORRECT TEACHER PASSWORD!", "The Password provided is incorrect");
                }
            } else {
                CustomAlert.showAlert(Alert.AlertType.WARNING, "Teacher not Found", "TEACHER DOES NOT EXIST!", "Teacher username does not exist");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            close(conn, statement, resultSet);
        }
        return teacher;
    }
}
