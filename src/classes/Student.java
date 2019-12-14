package classes;

public class Student extends Person {
    private String courses;

    public Student ( String name, String username, String password) {
        super(name, username, password);
    }

//    public Student (int id, String name, String username, String courses) {
//        super(id, name, username);
//        this.courses = courses;
//    }

    public Student (int id, String name, String username, String password, String courses) {
        super(id, name, username, password);
        this.courses = courses;
    }

    public String getCourses () {
        return this.courses;
    }

    public void setCourses(String title) {
        this.courses = title;
    }
}