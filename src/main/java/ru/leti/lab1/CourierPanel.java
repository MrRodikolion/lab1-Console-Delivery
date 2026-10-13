package ru.leti.lab1;

import java.util.List;
import java.util.Scanner;

public final class CourierPanel {
    private CourierPanel() {
    }

    public static void run(Scanner scanner, Courier courier, Delivery delivery) {
        boolean running = true;
        while (running) {
            System.out.println("\n=== Кабинет курьера: " + courier.getName() + " ===");
            System.out.println("1. Посмотреть заказы");
            System.out.println("2. Взять новый заказ в работу");
            System.out.println("3. Изменить статус");
            System.out.println("0. Выйти из аккаунта");
            System.out.print("Выберите действие: ");
            if (!scanner.hasNextLine()) {
                return;
            }
            switch (scanner.nextLine().trim()) {
                case "1" -> printOrders(courier, delivery);
                case "2" -> takeNewOrder(scanner, courier, delivery);
                case "3" -> updateStatusMenu(scanner, courier, delivery);
                case "0" -> running = false;
                default -> System.out.println("Неизвестный пункт меню.");
            }
        }
    }

    private static void updateStatusMenu(Scanner scanner, Courier courier, Delivery delivery) {
        System.out.println("\n1. Изменить статус заказа");
        System.out.println("2. Изменить статус курьера");
        System.out.println("0. Назад");
        System.out.print("Выберите действие: ");
        if (!scanner.hasNextLine()) {
            return;
        }
        switch (scanner.nextLine().trim()) {
            case "1" -> updateOrderStatus(scanner, courier, delivery);
            case "2" -> updateCourierStatus(scanner, courier, delivery);
            case "0" -> {
            }
            default -> System.out.println("Неизвестный пункт меню.");
        }
    }

    private static void takeNewOrder(Scanner scanner, Courier courier, Delivery delivery) {
        if (!courier.isFree()) {
            System.out.println("Сначала завершите текущую доставку.");
            return;
        }
        List<Order> orders = delivery.getAvailableNewOrders();
        if (orders.isEmpty()) {
            System.out.println("Новых заказов для назначения нет.");
            return;
        }
        System.out.println("\nНовые заказы:");
        for (int i = 0; i < orders.size(); i++) {
            System.out.println((i + 1) + ". " + orders.get(i));
        }
        System.out.print("Введите номер заказа: ");
        if (!scanner.hasNextLine()) {
            return;
        }
        int orderNumber = parseNumber(scanner.nextLine());
        if (orderNumber < 1 || orderNumber > orders.size()) {
            System.out.println("Заказ с таким номером не найден.");
            return;
        }
        if (delivery.claimOrder(orders.get(orderNumber - 1), courier)) {
            System.out.println("Заказ принят в работу.");
        } else {
            System.out.println("Не удалось принять заказ. Возможно, его уже взял другой курьер.");
        }
    }

    private static void updateCourierStatus(Scanner scanner, Courier courier, Delivery delivery) {
        System.out.println("Текущий статус курьера: " + (courier.isFree() ? "свободен" : "занят"));
        System.out.print("Введите новый статус (1 - свободен, 2 - занят): ");
        if (!scanner.hasNextLine()) {
            return;
        }
        boolean newStatus = switch (scanner.nextLine().trim()) {
            case "1" -> true;
            case "2" -> false;
            default -> {
                System.out.println("Неизвестный статус.");
                yield courier.isFree();
            }
        };
        if (newStatus == courier.isFree()) {
            return;
        }
        if (newStatus && delivery.hasActiveOrdersForCourier(courier)) {
            System.out.println("Нельзя стать свободным, пока есть незавершённая доставка.");
            return;
        }
        courier.setFree(newStatus);
        System.out.println("Статус курьера обновлён.");
    }

    private static void printOrders(Courier courier, Delivery delivery) {
        List<Order> orders = delivery.getOrdersForCourier(courier);
        System.out.println("\nВаши заказы:");
        if (orders.isEmpty()) {
            System.out.println("Заказов, назначенных вам, пока нет.");
            return;
        }
        for (int i = 0; i < orders.size(); i++) {
            System.out.println((i + 1) + ". " + orders.get(i));
        }
    }

    private static void updateOrderStatus(Scanner scanner, Courier courier, Delivery delivery) {
        List<Order> orders = delivery.getOrdersForCourier(courier);
        if (orders.isEmpty()) {
            System.out.println("У вас нет назначенных заказов.");
            return;
        }
        printOrders(courier, delivery);
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
        OrderStatus status = OrderStatusMenu.select(scanner, order, false);
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