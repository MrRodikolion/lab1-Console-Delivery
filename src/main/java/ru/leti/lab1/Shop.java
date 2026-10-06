package ru.leti.lab1;

import java.util.ArrayList;
import java.util.List;

public class Shop {
    private List<IProduct> products;

    public Shop() {
        this.products = new ArrayList<>();
    }

    public List<IProduct> getProducts() {
        return products;
    } 
}
