package ru.leti.lab1;

public record OrderItem(Product product, int quantity, long unitPrice) {
    public OrderItem {
        if (product == null) {
            throw new IllegalArgumentException("Товар не может быть null.");
        }
        if (quantity < 1) {
            throw new IllegalArgumentException("Количество должно быть положительным.");
        }
        if (unitPrice < 0) {
            throw new IllegalArgumentException("Цена не может быть отрицательной.");
        }
    }

    public long getTotalPrice() {
        return unitPrice * quantity;
    }
}