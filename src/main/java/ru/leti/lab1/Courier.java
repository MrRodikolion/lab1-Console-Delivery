package ru.leti.lab1;

public class Courier implements ICourier {
    private String status = "svo";
    private String login;
    private String password;
    String name;

    public Courier(String name, boolean isFree, String login, String password) {
        this.name = name;
        this.status = isFree ? "svo" : "занят";
        this.login = login;
        this.password = password;
    }

    public boolean matches(String login, String password) {
        return this.login.equals(login) && this.password.equals(password);
    }

    public boolean isFree() {
        return this.status.equals("svo");
    }

    public void changeStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return name;
    }
}

