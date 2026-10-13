package ru.leti.lab1;

public class PickupDelivery implements DeliveryMethod {
    @Override
    public String getName() {
        return "Самовывоз";
    }

    @Override
    public long calculateCost(long itemsSubtotal) {
        return 0;
    }

    @Override
    public int estimateDeliveryHours() {
        return 0;
    }

    @Override
    public boolean requiresCourier() {
        return false;
    }
}