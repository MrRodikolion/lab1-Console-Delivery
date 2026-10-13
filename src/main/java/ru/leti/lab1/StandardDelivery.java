package ru.leti.lab1;

public class StandardDelivery implements DeliveryMethod {
    private static final long COST = 200;
    private static final int DELIVERY_HOURS = 24;

    @Override
    public String getName() {
        return "Стандартная";
    }

    @Override
    public long calculateCost(long itemsSubtotal) {
        return COST;
    }

    @Override
    public int estimateDeliveryHours() {
        return DELIVERY_HOURS;
    }

    @Override
    public boolean requiresCourier() {
        return true;
    }
}