package ru.leti.lab1;

public interface DeliveryMethod {
    String getName();

    long calculateCost(long itemsSubtotal);

    int estimateDeliveryHours();

    boolean requiresCourier();
}