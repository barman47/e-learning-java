package models;

public class Admin extends User {
    public Admin () {
        super();
    }

    public Admin (String name, String username, String password) {
        super(name, username, password);
    }

    public Admin (int id, String name, String username, String password) {
        super(id, name, username, password);
    }
}
