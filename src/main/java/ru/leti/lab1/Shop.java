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

    public void addProduct(IProduct product) {
        products.add(product);
    }

    public IProduct addProduct2basket(int productId) {
        products.get(productId).decreaseQuantity(1);
        return products.get(productId);
    }
}
