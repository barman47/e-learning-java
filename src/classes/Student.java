package classes;

import java.util.ArrayList;

public class Student extends Person {
    private ArrayList<Course> courses;
    public Student (int id, String name, String username, String password, ArrayList<Course> courses) {
        super(id, name, username, password);
        this.courses = courses;
    }
}
