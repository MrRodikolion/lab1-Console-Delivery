package ru.leti.lab1;

public class Courier implements ICourier {
    private String status = "svo";
    public boolean isFree() {
        return this.status.equals("svo");
    }
    public void changeStatus(String status){
        this.status = status;
    }
}

