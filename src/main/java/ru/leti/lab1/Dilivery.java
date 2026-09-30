package ru.leti.lab1;

import java.util.List;
import java.util.ArrayList;

public class Dilivery {
    private List<Courrier> couriers;
    private List<Order> orders;

    public Dilivery() {
        this.couriers = new ArrayList<>();
        this.orders = new ArrayList<>();
    }

    public List<Courrier> getFreeCouriers() {
        List<Courrier> freeCouriers = new ArrayList<>();
        for (Courrier courier : couriers) {
            if (courier.isFree()) {
                freeCouriers.add(courier);
            }
        }
        return freeCouriers;
    }

    public List<Order> getOrders() {
        return orders;
    }
    
    public void updateOrderStatus(Order order, String status) {
        order.setStatus(status);
    }

    
}
