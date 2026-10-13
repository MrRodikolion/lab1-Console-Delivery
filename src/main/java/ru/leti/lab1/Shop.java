package ru.leti.lab1;

import java.util.ArrayList;
import java.util.List;

public class Shop {
    private final List<Product> products;

    public Shop() {
        this.products = new ArrayList<>();
    }

    public List<Product> getProducts() {
        return List.copyOf(products);
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public boolean removeFromStock(Product product, int quantity) {
        if (!products.contains(product) || quantity < 1 || product.getQuantity() < quantity) {
            return false;
        }
        product.decreaseQuantity(quantity);
        return true;
    }
}
