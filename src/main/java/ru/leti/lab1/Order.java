package ru.leti.lab1;

public class Order implements IOrder {
    private String status = "New_order";
    private final int id;
    private final String customerName;
    private final String productName;

    public Order(int id, String customerName, String productName, String status) {
        this.id = id;
        this.customerName = customerName;
        this.productName = productName;
        this.status = status;
    }

    public boolean belongsTo(User user) {
        return customerName.equals(user.name);
    }

    @Override
    public void setStatus(String newStatus) {
        this.status = newStatus;
    }

    public String getStatus() {
        return this.status;
    }

    @Override
    public String toString() {
        return "Заказ " + id + " | клиент: " + customerName + " | товар: " + productName + " | статус: " + status;
    }
}

