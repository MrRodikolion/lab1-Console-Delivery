package ru.leti.lab1;

public class Product implements IProduct {
    private final int price;
    private int amount;
    private String naming;

    public Product(String naming, int amount) {
        this(naming, amount, 0);
    }

    public Product(String naming, int amount, int price) {
        this.naming = naming;
        this.amount = Math.max(0, amount);
        this.price = Math.max(0, price);
    }

    public int getQuantity() {
        return amount;
    }

    public String getName() {
        return naming;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public void decreaseQuantity(int quantity) {
        if (quantity > 0) {
            amount = Math.max(0, amount - quantity);
        }
    }

    @Override
    public String toString() {
        return naming + " | цена: " + price + " руб. | в наличии: " + amount;
    }
}
