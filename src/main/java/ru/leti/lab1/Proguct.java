package ru.leti.lab1;

public class Product implements IProduct {
    private Integer price;
    private int amount;
    private String naming;
    @Override
    public void decreaseQuantity(int amount) {
        if (this.amount <= 0) {
            return;
        } else {
            this.amount -= 1;
        }
    }
}
