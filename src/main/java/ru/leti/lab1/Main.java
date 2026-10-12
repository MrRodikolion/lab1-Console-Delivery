package ru.leti.lab1;

void main()
{
        Shop shop = new Shop();

        Product product = new Product("Apple", 5);
        shop.addProduct(product);

        System.out.println("До покупки: " + product);
        shop.addProduct2basket(0);
        System.out.println("После покупки: " + product);

        Delivery delivery = new Delivery();
        Order order = new Order("New");
        delivery.updateOrderStatus(order, "In progress");
        System.out.println("Статус заказа: " + order);
    }

    private static class Product implements IProduct {
        private final String name;
        private int quantity;

        public Product(String name, int quantity) {
            this.name = name;
            this.quantity = quantity;
        }

        @Override
        public void decreaseQuantity(int amount) {
            quantity -= amount;
        }

        @Override
        public String toString() {
            return name + " (quantity=" + quantity + ")";
        }
    }

    private static class Order implements IOrder {
        private String status;

        public Order(String status) {
            this.status = status;
        }

        @Override
        public void setStatus(String status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return status;
        }
    }
