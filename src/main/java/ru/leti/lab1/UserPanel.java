package ru.leti.lab1;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public final class UserPanel {
    private UserPanel() {
    }

    public static void run(Scanner scanner, User user, Shop shop, Delivery delivery) {
        boolean running = true;
        while (running) {
            System.out.println("\n=== Кабинет пользователя: " + user.getName() + " ===");
            System.out.println("1. Посмотреть товары");
            System.out.println("2. Заказать товар");
            System.out.println("3. Мои заказы");
            System.out.println("0. Выйти из аккаунта");
            System.out.print("Выберите действие: ");
            if (!scanner.hasNextLine()) {
                return;
            }
            switch (scanner.nextLine().trim()) {
                case "1" -> printProducts(shop);
                case "2" -> createOrder(scanner, user, shop, delivery);
                case "3" -> printUserOrders(user, delivery);
                case "0" -> running = false;
                default -> System.out.println("Неизвестный пункт меню.");
            }
        }
    }

    private static void createOrder(Scanner scanner, User user, Shop shop, Delivery delivery) {
        List<Product> products = shop.getProducts();
        Map<Integer, Integer> cart = new LinkedHashMap<>();
        while (true) {
            printProductsAndCart(products, cart);
            System.out.print("Введите номер товара и количество (например, 1 3; количество по умолчанию 1; 0 - оформить): ");
            if (!scanner.hasNextLine()) {
                return;
            }
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Введите номер товара.");
                continue;
            }
            String[] selection = input.split("\\s+");
            int productNumber = parseNumber(selection[0]);
            if (productNumber == 0 && selection.length == 1) {
                break;
            }
            if (productNumber < 1 || productNumber > products.size()) {
                System.out.println("Товар с таким номером не найден.");
                continue;
            }
            if (selection.length > 2) {
                System.out.println("Введите номер товара и необязательное количество.");
                continue;
            }
            int quantity = selection.length == 2 ? parseNumber(selection[1]) : 1;
            if (quantity < 1) {
                System.out.println("Количество должно быть положительным целым числом.");
                continue;
            }

            Product product = products.get(productNumber - 1);
            int productIndex = productNumber - 1;
            int alreadySelected = cart.getOrDefault(productIndex, 0);
            if (quantity > product.getQuantity() - alreadySelected) {
                System.out.println("Недостаточно товара. Доступно для добавления: "
                        + (product.getQuantity() - alreadySelected) + ".");
                continue;
            }
            cart.merge(productIndex, quantity, Integer::sum);
        }

        if (cart.isEmpty()) {
            System.out.println("Заказ не оформлен: товары не выбраны.");
            return;
        }
        List<OrderItem> items = new ArrayList<>();
        long itemsSubtotal = 0;
        for (Map.Entry<Integer, Integer> item : cart.entrySet()) {
            Product product = products.get(item.getKey());
            OrderItem orderItem = new OrderItem(product, item.getValue(), product.getPrice());
            items.add(orderItem);
            itemsSubtotal += orderItem.getTotalPrice();
        }

        System.out.print("Адрес доставки (для самовывоза укажите «Пункт выдачи»): ");
        if (!scanner.hasNextLine()) {
            return;
        }
        String address = scanner.nextLine().trim();
        if (address.isEmpty()) {
            System.out.println("Адрес не может быть пустым.");
            return;
        }

        System.out.println("1. Стандартная доставка");
        System.out.println("2. Экспресс-доставка");
        System.out.println("3. Самовывоз");
        System.out.print("Выберите способ доставки: ");
        if (!scanner.hasNextLine()) {
            return;
        }
        DeliveryMethod deliveryMethod = switch (scanner.nextLine().trim()) {
            case "1" -> new StandardDelivery();
            case "2" -> new ExpressDelivery();
            case "3" -> new PickupDelivery();
            default -> null;
        };
        if (deliveryMethod == null) {
            System.out.println("Неизвестный способ доставки. Заказ не оформлен.");
            return;
        }

        long deliveryCost = deliveryMethod.calculateCost(itemsSubtotal);
        int deliveryHours = deliveryMethod.estimateDeliveryHours();
        System.out.println("\nПроверка заказа:");
        System.out.println("Товары: " + itemsSubtotal + " руб.");
        System.out.println("Доставка («" + deliveryMethod.getName() + "»): " + deliveryCost + " руб.");
        System.out.println("Адрес: " + address);
        System.out.println("Ориентировочный срок: "
                + (deliveryHours == 0 ? "самовывоз, без ожидания доставки" : deliveryHours + " ч."));
        System.out.println("Итого: " + (itemsSubtotal + deliveryCost) + " руб.");
        System.out.print("Оформить заказ? (д/н): ");
        if (!scanner.hasNextLine()) {
            System.out.println("Заказ не оформлен.");
            return;
        }
        String confirmation = scanner.nextLine().trim();
        if (!confirmation.equalsIgnoreCase("д") && !confirmation.equalsIgnoreCase("да")
                && !confirmation.equalsIgnoreCase("y")) {
            System.out.println("Заказ не оформлен.");
            return;
        }

        for (Map.Entry<Integer, Integer> item : cart.entrySet()) {
            if (!shop.removeFromStock(products.get(item.getKey()), item.getValue())) {
                System.out.println("Не удалось оформить заказ: остаток товара изменился.");
                return;
            }
        }
        int orderId = delivery.getOrders().stream().mapToInt(Order::getId).max().orElse(0) + 1;
        Order order = new Order(orderId, user, items, address, deliveryMethod);
        delivery.addOrder(order);
        System.out.println("Заказ " + orderId + " оформлен. Сумма: " + order.getTotalPrice() + " руб.");
        System.out.println(deliveryMethod.requiresCourier()
                ? "Заказ ожидает курьера." : "Заказ ожидает получения в пункте выдачи.");
    }

    private static void printProductsAndCart(List<Product> products, Map<Integer, Integer> cart) {
        List<Map.Entry<Integer, Integer>> cartItems = new ArrayList<>(cart.entrySet());
        System.out.printf("%-48s | %s%n", "Товары", "Текущая корзина");
        System.out.println("-".repeat(48) + "+" + "-".repeat(32));
        int rowCount = Math.max(products.size(), cartItems.size());
        for (int row = 0; row < rowCount; row++) {
            String productText = "";
            String cartText = "";
            if (row < products.size()) {
                Product product = products.get(row);
                int remaining = product.getQuantity() - cart.getOrDefault(row, 0);
                productText = (row + 1) + ". " + product.getName() + " | " + product.getPrice()
                        + " руб./шт. | доступно: " + remaining;
            }
            if (row < cartItems.size()) {
                Map.Entry<Integer, Integer> item = cartItems.get(row);
                Product product = products.get(item.getKey());
                cartText = product.getName() + " x " + item.getValue();
            }
            System.out.printf("%-48s | %s%n", productText, cartText);
        }
    }

    private static void printUserOrders(User user, Delivery delivery) {
        System.out.println("\nВаши заказы:");
        boolean found = false;
        for (Order order : delivery.getOrders()) {
            if (order.belongsTo(user)) {
                System.out.println(order);
                found = true;
            }
        }
        if (!found) {
            System.out.println("У вас пока нет заказов.");
        }
    }

    private static void printProducts(Shop shop) {
        System.out.println("\nТовары:");
        List<Product> products = shop.getProducts();
        for (int i = 0; i < products.size(); i++) {
            System.out.println((i + 1) + ". " + products.get(i));
        }
    }

    private static int parseNumber(String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException exception) {
            return -1;
        }
    }
}