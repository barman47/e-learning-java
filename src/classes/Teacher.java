package classes;

import java.util.ArrayList;

public class Teacher extends Person {
    private ArrayList<Course> courses;

    public Teacher (int id, String name, String username, String password) {
        super(id, name, username, password);
    }

    public Teacher ( String name, String username, String password) {
        super(name, username, password);
    }

    public Teacher (int id, String name, String username, ArrayList<Course> courses) {
        super(id, name, username);
        this.courses = courses;
    }

    public Teacher (int id, String name, String username, String password, ArrayList<Course> courses) {
        super(id, name, username, password);
        this.courses = courses;
    }

    public ArrayList<Course> getCourses () {
        return this.courses;
    }

    public void addCourse(String title) {
        this.courses.add(new Course(title));
    }
}
