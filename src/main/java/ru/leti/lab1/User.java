package ru.leti.lab1;

public class User {
    private final int id;
    String name;
    private final String phone;
    private final String login;
    private final String password;

    public User(int id, String name, String phone, String login, String password) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.login = login;
        this.password = password;
    }

    public boolean matches(String login, String password) {
        return this.login.equals(login) && this.password.equals(password);
    }

    @Override
    public String toString() {
        return id + ". " + name + " | " + phone;
    }
}