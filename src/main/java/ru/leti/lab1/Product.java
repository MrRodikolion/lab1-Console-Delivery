package ru.leti.lab1;

public class Product implements IProduct {
    private Integer price;
    private int amount;
    private String naming;

    public Product(String naming, int amount) {
        this.naming = naming;
        this.amount = Math.max(0, amount);
    }

    public int getQuantity() {
        return amount;
    }

    public String getName() {
        return naming;
    }

    @Override
    public void decreaseQuantity(int quantity) {
        if (quantity > 0) {
            amount = Math.max(0, amount - quantity);
        }
    }

    @Override
    public String toString() {
        return naming + " | в наличии: " + amount;
    }
}
