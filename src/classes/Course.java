package classes;

import java.util.ArrayList;

public class Course {
    private int id;
    private String students;
    private String courseUrl;
    private String title;

    public Course () {
    }

    public Course (int id, String title) {
        this.id = id;
        this.title = title;
    }

    public Course (int id, String students, String courseUrl, String title) {
        this.id = id;
        this.students = students;
        this.courseUrl = courseUrl;
        this.title = title;
    }

    public Course (String title) {
        this.title = title;
    }

    public int getId() {
        return this.id;
    }

    public void setId (int id) {
        this.id = id;
    }

    public String getStudents() {
        return this.students;
    }

    public void setStudentId (String studentId) {
        this.students = studentId;
    }

    public String getCourseUrl () {
        return this.courseUrl;
    }

    public void setCourseUrl (String courseUrl) {
        this.courseUrl = courseUrl;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle (String title) {
        this.title = title;
    }

}
