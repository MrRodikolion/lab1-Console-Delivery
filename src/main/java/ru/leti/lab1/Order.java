package ru.leti.lab1;

import java.util.AbstractList;
import java.util.ArrayList;

public class Order implements IOrder {
    private String status = "New_order";
    private ArrayList<String> listOfProducts;
    private int id;
    private String customerName;
    private String productName;

    private Order(int id, String customerName, String productName, String status) {
        this.id = id;
        this.customerName = customerName;
        this.productName = productName;
        this.status = status;
    }
    private boolean belongsTo() {
        return customerName.equals(customerName);
    }
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
    @Override
    public String toString() {
        return "Заказ " + id + " | клиент: " + customerName + " | товар: " + productName + " | статус: " + status;
    }

}
