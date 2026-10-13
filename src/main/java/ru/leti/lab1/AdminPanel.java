package ru.leti.lab1;

import java.util.List;
import java.util.Scanner;

public final class AdminPanel {
    private AdminPanel() {
    }

    public static void run(Scanner scanner, List<User> users, Shop shop, Delivery delivery) {
        boolean running = true;
        while (running) {
            System.out.println("\n=== Панель администратора службы доставки ===");
            System.out.println("1. Пользователи");
            System.out.println("2. Курьеры");
            System.out.println("3. Заказы");
            System.out.println("4. Товары");
            System.out.println("5. Изменить статус заказа");
            System.out.println("0. Выход");
            System.out.print("Выберите действие: ");
            if (!scanner.hasNextLine()) {
                return;
            }
            switch (scanner.nextLine().trim()) {
                case "1" -> printUsers(users);
                case "2" -> printCouriers(delivery);
                case "3" -> printOrders(delivery);
                case "4" -> printProducts(shop);
                case "5" -> updateOrderStatus(scanner, delivery);
                case "0" -> running = false;
                default -> System.out.println("Неизвестный пункт меню. Выберите пункт от 0 до 5.");
            }
        }
    }

    private static void printUsers(List<User> users) {
        System.out.println("\nПользователи:");
        for (User user : users) {
            System.out.println(user);
        }
    }

    private static void printCouriers(Delivery delivery) {
        System.out.println("\nКурьеры:");
        List<Courier> couriers = delivery.getCouriers();
        for (int i = 0; i < couriers.size(); i++) {
            Courier courier = couriers.get(i);
            String availability = courier.isFree() ? "свободен" : "занят";
            System.out.println((i + 1) + ". " + courier + " | " + availability);
        }
    }

    private static void printOrders(Delivery delivery) {
        System.out.println("\nЗаказы:");
        List<Order> orders = delivery.getOrders();
        for (int i = 0; i < orders.size(); i++) {
            System.out.println((i + 1) + ". " + orders.get(i));
        }
    }

    private static void printProducts(Shop shop) {
        System.out.println("\nТовары:");
        List<Product> products = shop.getProducts();
        for (int i = 0; i < products.size(); i++) {
            System.out.println((i + 1) + ". " + products.get(i));
        }
    }

    private static void updateOrderStatus(Scanner scanner, Delivery delivery) {
        List<Order> orders = delivery.getOrders();
        if (orders.isEmpty()) {
            System.out.println("Заказов пока нет.");
            return;
        }
        printOrders(delivery);
        System.out.print("Введите номер заказа: ");
        if (!scanner.hasNextLine()) {
            return;
        }
        int orderNumber = parseNumber(scanner.nextLine());
        if (orderNumber < 1 || orderNumber > orders.size()) {
            System.out.println("Заказ с таким номером не найден.");
            return;
        }

        Order order = orders.get(orderNumber - 1);
        OrderStatus status = OrderStatusMenu.select(scanner, order, true);
        if (status == null) {
            return;
        }
        if (delivery.updateOrderStatus(order, status)) {
            System.out.println("Статус заказа обновлён.");
        } else {
            System.out.println("Недопустимый переход статуса.");
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