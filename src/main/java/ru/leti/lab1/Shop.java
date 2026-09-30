package ru.leti.lab1;

import java.util.ArrayList;
import java.util.List;

public class Shop {
    private List<Product> products;

    public Shop() {
        this.products = new ArrayList<>();
    }

    public List<Product> getProducts() {
        return products;
    } 
}
