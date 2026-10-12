package ru.leti.lab1;

public class Courier implements ICourier {
    private String status = "svo";
    private String login;
    private String password;
    private String name;

    private Courier(String name, String login, String password )
    public boolean isFree() {
        return this.status.equals("svo");
    }
    public void changeStatus(String status){
        this.status = status;
    }
}

