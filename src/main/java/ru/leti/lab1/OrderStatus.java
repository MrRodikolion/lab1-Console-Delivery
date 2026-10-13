package ru.leti.lab1;

public enum OrderStatus {
    NEW("Новый"),
    ASSIGNED("Принят курьером"),
    IN_TRANSIT("В пути"),
    DELIVERED("Доставлен"),
    CANCELLED("Отменен");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean canTransitionTo(OrderStatus next) {
        return switch (this) {
            case NEW -> next == ASSIGNED || next == CANCELLED;
            case ASSIGNED -> next == IN_TRANSIT || next == CANCELLED;
            case IN_TRANSIT -> next == DELIVERED;
            case DELIVERED, CANCELLED -> false;
        };
    }

    @Override
    public String toString() {
        return displayName;
    }
}