package ru.leti.lab1;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public final class OrderStatusMenu {
    private OrderStatusMenu() {
    }

    public static OrderStatus select(Scanner scanner, Order order, boolean allowCancellation) {
        List<OrderStatus> availableStatuses = new ArrayList<>();
        for (OrderStatus status : OrderStatus.values()) {
            if (order.getStatus().canTransitionTo(status)
                    && (allowCancellation || status != OrderStatus.CANCELLED)
                    && (status != OrderStatus.ASSIGNED || order.getCourier() != null)) {
                availableStatuses.add(status);
            }
        }
        if (availableStatuses.isEmpty()) {
            System.out.println("Для текущего статуса нет допустимых переходов.");
            return null;
        }
        System.out.println("Допустимые статусы для заказа «" + order.getStatus() + "»:");
        for (int i = 0; i < availableStatuses.size(); i++) {
            System.out.println((i + 1) + ". " + availableStatuses.get(i).getDisplayName());
        }
        System.out.print("Выберите новый статус: ");
        if (!scanner.hasNextLine()) {
            return null;
        }
        int statusNumber = parseNumber(scanner.nextLine());
        if (statusNumber < 1 || statusNumber > availableStatuses.size()) {
            System.out.println("Неизвестный пункт меню.");
            return null;
        }
        return availableStatuses.get(statusNumber - 1);
    }

    private static int parseNumber(String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException exception) {
            return -1;
        }
    }
}