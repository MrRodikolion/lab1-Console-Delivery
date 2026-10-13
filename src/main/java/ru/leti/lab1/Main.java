package ru.leti.lab1;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Shop shop = new Shop();
        Delivery delivery = new Delivery();
        List<User> users = createTemporaryUsers();
        seedTemporaryData(users, shop, delivery);

        try (Scanner scanner = new Scanner(System.in)) {
            runApplication(scanner, users, shop, delivery);
        }
    }

    private static void runApplication(Scanner scanner, List<User> users, Shop shop, Delivery delivery) {
        while (true) {
            printLoginMenu();
            if (!scanner.hasNextLine()) {
                return;
            }
            switch (scanner.nextLine().trim()) {
                case "1" -> loginAdmin(scanner, users, shop, delivery);
                case "2" -> loginUser(scanner, users, shop, delivery);
                case "3" -> loginCourier(scanner, delivery);
                case "0" -> {
                    System.out.println("Работа приложения завершена.");
                    return;
                }
                default -> System.out.println("Неизвестный пункт меню. Выберите пункт от 0 до 3.");
            }
        }
    }

    private static void printLoginMenu() {
        System.out.println("\n=== Служба доставки ===");
        System.out.println("1. Войти как администратор");
        System.out.println("2. Войти как пользователь");
        System.out.println("3. Войти как курьер");
        System.out.println("0. Выход");
        System.out.println("Демо-входы: admin/admin123, anna/anna123, ivan/ivan123, mike/mike123, elena/elena123");
        System.out.print("Выберите роль: ");
    }

    private static void loginAdmin(Scanner scanner, List<User> users, Shop shop, Delivery delivery) {
        if (authenticate(scanner, "admin", "admin123")) {
            AdminPanel.run(scanner, users, shop, delivery);
        } else {
            System.out.println("Неверный логин или пароль.");
        }
    }

    private static void loginUser(Scanner scanner, List<User> users, Shop shop, Delivery delivery) {
        User user = authenticateUser(scanner, users);
        if (user == null) {
            System.out.println("Неверный логин или пароль.");
            return;
        }
        UserPanel.run(scanner, user, shop, delivery);
    }

    private static void loginCourier(Scanner scanner, Delivery delivery) {
        Courier courier = authenticateCourier(scanner, delivery);
        if (courier == null) {
            System.out.println("Неверный логин или пароль.");
            return;
        }
        CourierPanel.run(scanner, courier, delivery);
    }

    private static boolean authenticate(Scanner scanner, String expectedLogin, String expectedPassword) {
        System.out.print("Логин: ");
        if (!scanner.hasNextLine()) {
            return false;
        }
        String login = scanner.nextLine().trim();
        System.out.print("Пароль: ");
        if (!scanner.hasNextLine()) {
            return false;
        }
        String password = scanner.nextLine().trim();
        return expectedLogin.equals(login) && expectedPassword.equals(password);
    }

    private static User authenticateUser(Scanner scanner, List<User> users) {
        System.out.print("Логин: ");
        if (!scanner.hasNextLine()) {
            return null;
        }
        String login = scanner.nextLine().trim();
        System.out.print("Пароль: ");
        if (!scanner.hasNextLine()) {
            return null;
        }
        String password = scanner.nextLine().trim();
        for (User user : users) {
            if (user.matches(login, password)) {
                return user;
            }
        }
        return null;
    }

    private static Courier authenticateCourier(Scanner scanner, Delivery delivery) {
        System.out.print("Логин: ");
        if (!scanner.hasNextLine()) {
            return null;
        }
        String login = scanner.nextLine().trim();
        System.out.print("Пароль: ");
        if (!scanner.hasNextLine()) {
            return null;
        }
        String password = scanner.nextLine().trim();
        for (Courier courier : delivery.getCouriers()) {
            if (courier.matches(login, password)) {
                return courier;
            }
        }
        return null;
    }

    private static List<User> createTemporaryUsers() {
        return List.of(
                new User(1, "Анна Смирнова", "+7 900 111-22-33", "anna", "anna123"),
                new User(2, "Иван Петров", "+7 900 444-55-66", "ivan", "ivan123"));
    }

    private static void seedTemporaryData(List<User> users, Shop shop, Delivery delivery) {
        shop.addProduct(new Product("Яблоки", 12, 120));
        shop.addProduct(new Product("Хлеб", 7, 50));
        Courier mike = new Courier("Михаил", true, "mike", "mike123");
        Courier elena = new Courier("Елена", true, "elena", "elena123");
        delivery.addCourier(mike);
        delivery.addCourier(elena);

        Product apples = shop.getProducts().get(0);
        Product bread = shop.getProducts().get(1);
        delivery.addOrder(new Order(1, users.get(0),
                List.of(new OrderItem(apples, 1, apples.getPrice())),
                "ул. Примерная, 1", new StandardDelivery()));
        Order inDelivery = new Order(2, users.get(1),
                List.of(new OrderItem(bread, 1, bread.getPrice())),
                "ул. Примерная, 2", new StandardDelivery());
        delivery.addOrder(inDelivery);
        delivery.claimOrder(inDelivery, elena);
    }
}