package ru.leti.lab1;

public class Courier implements ICourier {
    private boolean free;
    private final String login;
    private final String password;
    String name;

    public Courier(String name, boolean isFree, String login, String password) {
        this.name = name;
        this.free = isFree;
        this.login = login;
        this.password = password;
    }

    public boolean matches(String login, String password) {
        return this.login.equals(login) && this.password.equals(password);
    }

    @Override
    public boolean isFree() {
        return free;
    }

    public void changeStatus(boolean free) {
        this.free = free;
    }

    @Override
    public String toString() {
        return name;
    }
}

