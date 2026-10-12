package ru.leti.lab1;

public class Product implements IProduct {
    private int price;
    private int amount;
    private String name;

    private Product(String name, int amount, int price) {
        this.name = name;
        this.amount = amount;
        this.price = price;
    }

    private String getName() {
        return name;
    }

    private int getAmount() {
        return amount;
    }

    ;

    @Override
    public void decreaseQuantity(int amount) {
        if (this.amount <= 0) {
            return;
        } else {
            this.amount -= 1;
        }
    }

    @Override
    public String toString() {
        return name + " (количество=" + amount + ")";
    }
}
