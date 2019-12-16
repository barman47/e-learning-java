package models;

import javafx.scene.control.Alert;
import sample.CustomAlert;

import java.sql.*;
import java.util.ArrayList;

public class CourseDataStore {
    public static final String DB_NAME = "elearning.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:C:\\databases\\" + DB_NAME;

    // Course Database
    public static final String TABLE_COURSE = "courses";
    public static final String COLUMN_COURSE_ID = "id";
    public static final String COLUMN_STUDENTS = "students";
    public static final String COLUMN_COURSE_TITLE = "title";
    public static final String COLUMN_COURSE_URL = "url";
    public static final String CREATE_COURSE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_COURSE  +
            " (" + COLUMN_COURSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_STUDENTS + " TEXT, " +
            COLUMN_COURSE_URL +  " TEXT NOT NULL, " +
            COLUMN_COURSE_TITLE + " TEXT NOT NULL);";
    // Create course
    public static final String INSERT_COURSE = "INSERT INTO " + TABLE_COURSE + "(" + COLUMN_COURSE_TITLE + ", " + COLUMN_COURSE_URL + ") VALUES(?, ?)";

    private static final String FIND_COURSE = "SELECT *  FROM " + TABLE_COURSE + " WHERE " + COLUMN_COURSE_TITLE + " = ?;";

    private static final String FIND_COURSES = "SELECT *  FROM " + TABLE_COURSE + ";";

    // TEACHER DATABASE
//    public static final String TABLE_TEACHER = "teachers";
//    public static final String

    private Connection conn;
    private Statement statement;
    private ResultSet resultSet;

    private PreparedStatement insertCourse;
    private PreparedStatement findCourse;

    public boolean open () {
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING);
            // creating the course table
            createCourseTable(); // Must come before insert, otherwise there may be no table to insert into
            findCourse = conn.prepareStatement(FIND_COURSE);
            insertCourse = conn.prepareStatement(INSERT_COURSE);
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

    private void createCourseTable () {
        try {
            statement = conn.createStatement();
            statement.execute(CREATE_COURSE_TABLE);
        } catch (SQLException ex) {
            CustomAlert.showAlert(Alert.AlertType.ERROR, "Error", "ERROR CREATING COURSE TABLE", "Please try again!");
        }
    }

    public String insertCourse (String courseTitle, String fileUrl){
        String returnMessage = "";
        try {
            Course course = findCourse(courseTitle);
            if (course != null) {
                returnMessage = "Course already exists!";
            } else {
                open();
                insertCourse.setString(1, courseTitle);
                insertCourse.setString(2, fileUrl);
                insertCourse.execute();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            CustomAlert.showAlert(Alert.AlertType.ERROR, "Error", "ERROR INSERTING COURSE", "Please try again!");
        }

        return returnMessage;
    }

    public Course findCourse (String courseTitle) {
        Course course = new Course();
        ResultSet resultSet = null;
        try {
            findCourse.setString(1, courseTitle);
            resultSet = findCourse.executeQuery();
            while (resultSet.next()) {
                course.setId(resultSet.getInt("id"));
                course.setCourseUrl(resultSet.getString("url"));
                course.setTitle(resultSet.getString("title"));
                break;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            CustomAlert.showAlert(Alert.AlertType.ERROR, "Error", "ERROR EXECUTING QUERY", "Please try again!");
        } finally {
            close(conn, findCourse, resultSet);
        }
        return course;
    }

    public ArrayList<Course> getCourses () {
        ArrayList<Course> courses = new ArrayList<>();
        try {
            statement = conn.createStatement();
            resultSet = statement.executeQuery(FIND_COURSES);

            while (resultSet.next()) {
                courses.add(new Course(resultSet.getInt("id"), resultSet.getString("students"), resultSet.getString("url"), resultSet.getString("title")));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            CustomAlert.showAlert(Alert.AlertType.ERROR, "Error", "ERROR FETCHING COURSES", "Please try again!");
        } finally {
            close(conn, statement, resultSet);
        }
        return courses;
    }
}