package models;

public class User {
    private int id;
    private String name;
    private String username;
    private String password;

    public User() {
    }

    public User(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public User(int id, String name, String username, String password) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public User(int id, String name, String username) {
        this.id = id;
        this.name = name;
        this.username = username;
    }


    public void setId (int id) {
        this.id = id;
    }

    public int getId () {
        return this.id;
    }

    public void setName (String name) {
        this.name = name;
    }

    public String getName () {
        return this.name;
    }

    public void setUsername (String username) {
        this.username = username;
    }

    public String getUsername () {
        return this.username;
    }

    public void setPasword (String password) {
        this.password = password;
    }

    public String getPassword () {
        return this.password;
    }
}
