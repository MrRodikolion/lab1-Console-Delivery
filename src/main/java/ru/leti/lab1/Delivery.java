package ru.leti.lab1;

import java.util.List;
import java.util.ArrayList;

public class Delivery {
    private List<ICourier> couriers;
    private List<IOrder> orders;

    public Delivery() {
        this.couriers = new ArrayList<>();
        this.orders = new ArrayList<>();
    }

    public List<ICourier> getFreeCouriers() {
        List<ICourier> freeCouriers = new ArrayList<>();
        for (ICourier courier : couriers) {
            if (courier.isFree()) {
                freeCouriers.add(courier);
            }
        }
        return freeCouriers;
    }

    public List<IOrder> getOrders() {
        return orders;
    }

    public List<ICourier> getCouriers() {
        return couriers;
    }

    public void addCourier(ICourier courier) {
        couriers.add(courier);
    }

    public void addOrder(IOrder order) {
        orders.add(order);
    }

    public void updateOrderStatus(IOrder order, String status) {
        order.setStatus(status);
    }
}