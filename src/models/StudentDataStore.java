package models;

import javafx.scene.control.Alert;
import sample.CustomAlert;

import java.sql.*;
import java.util.ArrayList;

public class StudentDataStore {
    public static final String DB_NAME = "elearning.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:C:\\databases\\" + DB_NAME;

    // Student Database
    public static final String TABLE_STUDENT = "students";
    public static final String COLUMN_STUDENT_ID = "id";
    public static final String COLUMN_STUDENT_NAME = "name";
    public static final String COLUMN_STUDENT_USERNAME = "username";
    public static final String COLUMN_STUDENT_PASSWORD = "password";
    private static final String COLUMN_COURSES = "courses";
    public static final String CREATE_STUDENT_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_STUDENT  +
            " (" + COLUMN_STUDENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_STUDENT_NAME + " TEXT NOT NULL, " +
            COLUMN_STUDENT_USERNAME + " TEXT NOT NULL, " +
            COLUMN_COURSES + " TEXT, " +
            COLUMN_STUDENT_PASSWORD + " TEXT NOT NULL);";
    // Create student
    public static final String INSERT_STUDENT = "INSERT INTO " + TABLE_STUDENT + "(" + COLUMN_STUDENT_NAME + ", " +
            COLUMN_STUDENT_USERNAME + "," + COLUMN_STUDENT_PASSWORD + ") VALUES(?, ?, ?)";
    private static final String FIND_STUDENT = "SELECT *  FROM " + TABLE_STUDENT + " WHERE " + COLUMN_STUDENT_USERNAME + " = ?;";

    private static final String FIND_STUDENTS = "SELECT * FROM " + TABLE_STUDENT + ";";

    private static final String UPDATE_STUDENT = "UPDATE " + TABLE_STUDENT + " SET " + COLUMN_STUDENT_NAME + " = ?, " +
            COLUMN_STUDENT_USERNAME + " = ?, " + COLUMN_STUDENT_PASSWORD + " = ? " +
            "WHERE " + COLUMN_STUDENT_ID +  " = ?;";

    // TEACHER DATABASE
//    public static final String TABLE_TEACHER = "teachers";
//    public static final String

    private Connection conn;
    private Statement statement;
    private ResultSet resultSet;

    private PreparedStatement insertStudent;
    private PreparedStatement findStudent;
    private PreparedStatement updateStudent;

    public boolean open () {
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING);
            // creating the student table
            createStudentTable(); // Must come before insert, otherwise there may be no table to insert into
            findStudent = conn.prepareStatement(FIND_STUDENT);
            insertStudent = conn.prepareStatement(INSERT_STUDENT);
            updateStudent = conn.prepareStatement(UPDATE_STUDENT);
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

    private void createStudentTable () {
        try {
            statement = conn.createStatement();
            statement.execute(CREATE_STUDENT_TABLE);
        } catch (SQLException ex) {
            CustomAlert.showAlert(Alert.AlertType.ERROR, "Error", "ERROR CREATING STUDENT TABLE", "Please try again!");
        }
    }

    public String insertStudent (String name, String username, String password){
        String returnMessage = "";
        try {
            boolean studentExists = findStudent(username);
            if (studentExists) {
                returnMessage = "Username exists!";
            } else {
                open();
                insertStudent.setString(1, name);
                insertStudent.setString(2, username);
                insertStudent.setString(3, password);
                insertStudent.execute();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            CustomAlert.showAlert(Alert.AlertType.ERROR, "Error", "ERROR INSERTING STUDENT", "Please try again!");
        }

        return returnMessage;
    }

    public boolean findStudent (String username) {
        boolean student = false;
        ResultSet resultSet = null;
        try {
            findStudent.setString(1, username);
            resultSet = findStudent.executeQuery();
            if (resultSet.next()) {
                student = true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            CustomAlert.showAlert(Alert.AlertType.ERROR, "Error", "ERROR EXECUTING QUERY", "Please try again!");
        } finally {
            close(conn, findStudent, resultSet);
        }
        return student;
    }

    public Student loginStudent (String username, String password) {
        Student student = null;

        try {
            findStudent.setString(1, username);
            resultSet = findStudent.executeQuery();
            if (resultSet.next()) {
                student = new Student(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("username"), resultSet.getString("password"), resultSet.getString("courses"));
                if (!password.equals(student.getPassword())) {
                    CustomAlert.showAlert(Alert.AlertType.WARNING, "Password Incorrect", "INCORRECT STUDENT PASSWORD!", "The Password provided is incorrect");
                }
            } else {
                CustomAlert.showAlert(Alert.AlertType.WARNING, "Student not Found", "STUDENT DOES NOT EXIST!", "Student username does not exist");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            close(conn, statement, resultSet);
        }
        return student;
    }

    public ArrayList<Student> getStudents () {
        ArrayList<Student> students = new ArrayList<>();
        try {
            statement = conn.createStatement();
            resultSet = statement.executeQuery(FIND_STUDENTS);

            while (resultSet.next()) {
                students.add(new Student(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("username"), resultSet.getString("password"), resultSet.getString("courses")));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            CustomAlert.showAlert(Alert.AlertType.ERROR, "Error", "ERROR FETCHING STUDENTS", "Please try again!");
        } finally {
            close(conn, statement, resultSet);
        }
        return students;
    }

    public boolean updateStudent (int id, String name, String username, String password){
        System.out.println(name);
        System.out.println(username);
        System.out.println(password);
        System.out.println(id);
        boolean isQuerySuccessful = true;
        try{
            updateStudent.setString(1, name);
            updateStudent.setString(2, username);
            updateStudent.setString(3, password);
            updateStudent.setInt(4, id);
            updateStudent.execute();

        } catch (SQLException ex) {
            ex.printStackTrace();
            CustomAlert.showAlert(Alert.AlertType.ERROR, "Error", "ERROR UPDATING STUDENT", "Please try again!");
            isQuerySuccessful = false;
        }

        return isQuerySuccessful;
    }
}
