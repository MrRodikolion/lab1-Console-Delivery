package ru.leti.lab1;

public class Order implements IOrder {
    private String status = "New_order";
    private final int id;
    private final String customerName;
    private final String productName;
    private final long totalPrice;
    private Courier courier;

    public Order(int id, String customerName, String productName, String status) {
        this(id, customerName, productName, status, 0);
    }

    public Order(int id, String customerName, String productName, String status, long totalPrice) {
        this.id = id;
        this.customerName = customerName;
        this.productName = productName;
        this.status = status;
        this.totalPrice = totalPrice;
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

    public Courier getCourier() {
        return courier;
    }

    public void assignCourier(Courier courier) {
        this.courier = courier;
    }

    @Override
    public String toString() {
        return "Заказ " + id + " | клиент: " + customerName + " | товар: " + productName
            + " | сумма: " + totalPrice + " руб. | курьер: "
            + (courier == null ? "не назначен" : courier.getName()) + " | статус: " + status;
    }
}

