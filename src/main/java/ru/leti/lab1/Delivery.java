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

    public List<IOrder> getAvailableNewOrders() {
        List<IOrder> availableOrders = new ArrayList<>();
        for (IOrder order : orders) {
            if (order instanceof Order deliveryOrder
                    && "Новый".equals(deliveryOrder.getStatus())
                    && deliveryOrder.getCourier() == null) {
                availableOrders.add(order);
            }
        }
        return availableOrders;
    }

    public boolean claimOrder(IOrder order, Courier courier) {
        if (!courier.isFree() || !(order instanceof Order deliveryOrder)
                || !"Новый".equals(deliveryOrder.getStatus())
                || deliveryOrder.getCourier() != null) {
            return false;
        }
        deliveryOrder.assignCourier(courier);
        deliveryOrder.setStatus("Принят курьером");
        courier.setFree(false);
        return true;
    }

    public List<IOrder> getOrdersForCourier(Courier courier) {
        List<IOrder> courierOrders = new ArrayList<>();
        for (IOrder order : orders) {
            if (order instanceof Order deliveryOrder && deliveryOrder.getCourier() == courier) {
                courierOrders.add(order);
            }
        }
        return courierOrders;
    }

    public boolean hasActiveOrdersForCourier(Courier courier) {
        for (IOrder order : getOrdersForCourier(courier)) {
            if (order instanceof Order deliveryOrder && !"Доставлен".equals(deliveryOrder.getStatus())) {
                return true;
            }
        }
        return false;
    }

    public void updateOrderStatus(IOrder order, String status) {
        order.setStatus(status);
        if ("Доставлен".equals(status) && order instanceof Order deliveryOrder
                && deliveryOrder.getCourier() != null) {
            deliveryOrder.getCourier().setFree(true);
        }
    }
}