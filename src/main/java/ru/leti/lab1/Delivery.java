package ru.leti.lab1;

import java.util.ArrayList;
import java.util.List;

public class Delivery {
    private final List<Courier> couriers = new ArrayList<>();
    private final List<Order> orders = new ArrayList<>();

    public List<Courier> getFreeCouriers() {
        return couriers.stream().filter(Courier::isFree).toList();
    }

    public List<Order> getOrders() {
        return List.copyOf(orders);
    }

    public List<Courier> getCouriers() {
        return List.copyOf(couriers);
    }

    public void addCourier(Courier courier) {
        couriers.add(courier);
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public List<Order> getAvailableNewOrders() {
        return orders.stream()
                .filter(order -> order.getStatus() == OrderStatus.NEW)
                .filter(order -> order.getDeliveryMethod().requiresCourier())
                .filter(order -> order.getCourier() == null)
                .toList();
    }

    public boolean claimOrder(Order order, Courier courier) {
        if (!orders.contains(order) || courier == null || !couriers.contains(courier)
                || !courier.isFree() || order.getStatus() != OrderStatus.NEW
                || !order.getDeliveryMethod().requiresCourier() || order.getCourier() != null
                || !order.assignCourier(courier)) {
            return false;
        }
        courier.setFree(false);
        return true;
    }

    public List<Order> getOrdersForCourier(Courier courier) {
        return orders.stream().filter(order -> order.getCourier() == courier).toList();
    }

    public boolean hasActiveOrdersForCourier(Courier courier) {
        return getOrdersForCourier(courier).stream()
                .anyMatch(order -> order.getStatus() != OrderStatus.DELIVERED
                        && order.getStatus() != OrderStatus.CANCELLED);
    }

    public boolean updateOrderStatus(Order order, OrderStatus status) {
        if (!orders.contains(order) || !order.transitionTo(status)) {
            return false;
        }
        if (status == OrderStatus.DELIVERED && order.getCourier() != null) {
            order.getCourier().setFree(true);
        } else if (status == OrderStatus.CANCELLED && order.getCourier() != null) {
            order.getCourier().setFree(true);
        }
        return true;
    }
}