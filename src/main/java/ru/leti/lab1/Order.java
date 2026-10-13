package ru.leti.lab1;

import java.util.List;
import java.util.Objects;

public class Order {
    private final int id;
    private final User customer;
    private final List<OrderItem> items;
    private final String deliveryAddress;
    private final DeliveryMethod deliveryMethod;
    private final long itemsSubtotal;
    private final long deliveryCost;
    private OrderStatus status = OrderStatus.NEW;
    private Courier courier;

    public Order(int id, User customer, List<OrderItem> items, String deliveryAddress,
            DeliveryMethod deliveryMethod) {
        if (id < 1) {
            throw new IllegalArgumentException("Номер заказа должен быть положительным.");
        }
        this.id = id;
        this.customer = Objects.requireNonNull(customer, "Клиент не может быть null.");
        this.items = List.copyOf(items);
        if (this.items.isEmpty()) {
            throw new IllegalArgumentException("Заказ должен содержать хотя бы одну позицию.");
        }
        this.deliveryAddress = Objects.requireNonNull(deliveryAddress, "Адрес не может быть null.");
        this.deliveryMethod = Objects.requireNonNull(deliveryMethod, "Способ доставки не может быть null.");
        this.itemsSubtotal = this.items.stream().mapToLong(OrderItem::getTotalPrice).sum();
        this.deliveryCost = deliveryMethod.calculateCost(itemsSubtotal);
    }

    public int getId() {
        return id;
    }

    public User getCustomer() {
        return customer;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public DeliveryMethod getDeliveryMethod() {
        return deliveryMethod;
    }

    public long getItemsSubtotal() {
        return itemsSubtotal;
    }

    public long getDeliveryCost() {
        return deliveryCost;
    }

    public long getTotalPrice() {
        return itemsSubtotal + deliveryCost;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public boolean belongsTo(User user) {
        return customer == user;
    }

    public Courier getCourier() {
        return courier;
    }

    public boolean transitionTo(OrderStatus next) {
        if (!status.canTransitionTo(next) || (next == OrderStatus.ASSIGNED && courier == null)) {
            return false;
        }
        status = next;
        return true;
    }

    public boolean assignCourier(Courier courier) {
        if (courier == null || !deliveryMethod.requiresCourier() || this.courier != null
                || !status.canTransitionTo(OrderStatus.ASSIGNED)) {
            return false;
        }
        this.courier = courier;
        if (!transitionTo(OrderStatus.ASSIGNED)) {
            this.courier = null;
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String products = items.stream()
                .map(item -> item.product().getName() + " x " + item.quantity())
                .collect(java.util.stream.Collectors.joining(", "));
        return "Заказ " + id + " | клиент: " + customer.getName() + " | товары: " + products
                + " | адрес: " + deliveryAddress + " | способ: " + deliveryMethod.getName()
                + " | стоимость товаров: " + itemsSubtotal + " руб. | доставка: " + deliveryCost
                + " руб. | сумма: " + getTotalPrice() + " руб. | курьер: "
                + (courier == null ? "не назначен" : courier.getName()) + " | статус: " + status;
    }
}

