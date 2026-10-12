package ru.leti.lab1;

import java.util.AbstractList;
import java.util.ArrayList;

public class Order implements IOrder {
    private String status = "New_order";
    private ArrayList<String> listOfProducts;

    public void init(){
        this.status = "New_order";
        this.listOfProducts = new ArrayList<>();
    }
    @Override
    public void setStatus(String newStatus) {
        this.status = newStatus;
    }
    public String getStatus()
    {
        return this.status;
    }
    public AbstractList<String> getArray()
    {
        return this.listOfProducts;
    }

}
