package ru.leti.lab1;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Shop shop = new Shop();
        Delivery delivery = new Delivery();
        List<User> users = createTemporaryUsers();
        seedTemporaryData(shop, delivery);

        try (Scanner scanner = new Scanner(System.in)) {
            runApplication(scanner, users, shop, delivery);
        }
    }

    private static void runApplication(Scanner scanner, List<User> users, Shop shop, Delivery delivery) {
        while (scanner.hasNextLine()) {
            printLoginMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
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
            runAdminPanel(scanner, users, shop, delivery);
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
        runUserPanel(scanner, user, shop, delivery);
    }

    private static void loginCourier(Scanner scanner, Delivery delivery) {
        Courier courier = authenticateCourier(scanner, delivery);
        if (courier == null) {
            System.out.println("Неверный логин или пароль.");
            return;
        }
        runCourierPanel(scanner, courier, delivery);
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
        for (ICourier courier : delivery.getCouriers()) {
            Courier temporaryCourier = (Courier) courier;
            if (temporaryCourier.matches(login, password)) {
                return temporaryCourier;
            }
        }
        return null;
    }

    private static void runAdminPanel(Scanner scanner, List<User> users, Shop shop, Delivery delivery) {
        boolean running = true;
        while (running) {
            printMenu();
            if (!scanner.hasNextLine()) {
                return;
            }

            String choice = scanner.nextLine().trim();
            switch (choice) {
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

    private static void runUserPanel(Scanner scanner, User user, Shop shop, Delivery delivery) {
        boolean running = true;
        while (running) {
            System.out.println("\n=== Кабинет пользователя: " + user.name + " ===");
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
        List<IProduct> products = shop.getProducts();
        printProducts(shop);
        System.out.print("Введите номер товара: ");
        if (!scanner.hasNextLine()) {
            return;
        }
        int productNumber = parseNumber(scanner.nextLine());
        if (productNumber < 1 || productNumber > products.size()) {
            System.out.println("Товар с таким номером не найден.");
            return;
        }

        IProduct selectedProduct = products.get(productNumber - 1);
        Product product = (Product) selectedProduct;
        if (product.getQuantity() <= 0) {
            System.out.println("Этого товара нет в наличии.");
            return;
        }

        shop.addProduct2basket(productNumber - 1);
        int orderId = delivery.getOrders().size() + 1;
        delivery.addOrder(new Order(orderId, user.name, product.getName(), "Новый"));
        System.out.println("Заказ " + orderId + " оформлен.");
    }

    private static void printUserOrders(User user, Delivery delivery) {
        System.out.println("\nВаши заказы:");
        boolean found = false;
        for (IOrder order : delivery.getOrders()) {
            Order temporaryOrder = (Order) order;
            if (temporaryOrder.belongsTo(user)) {
                System.out.println(temporaryOrder);
                found = true;
            }
        }
        if (!found) {
            System.out.println("У вас пока нет заказов.");
        }
    }

    private static void runCourierPanel(Scanner scanner, Courier courier, Delivery delivery) {
        boolean running = true;
        while (running) {
            System.out.println("\n=== Кабинет курьера: " + courier.name + " ===");
            System.out.println("1. Посмотреть заказы");
            System.out.println("2. Обновить статус заказа");
            System.out.println("0. Выйти из аккаунта");
            System.out.print("Выберите действие: ");
            if (!scanner.hasNextLine()) {
                return;
            }
            switch (scanner.nextLine().trim()) {
                case "1" -> printOrders(delivery);
                case "2" -> updateCourierOrderStatus(scanner, delivery);
                case "0" -> running = false;
                default -> System.out.println("Неизвестный пункт меню.");
            }
        }
    }

    private static void updateCourierOrderStatus(Scanner scanner, Delivery delivery) {
        List<IOrder> orders = delivery.getOrders();
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

        System.out.println("1. Принят курьером");
        System.out.println("2. В пути");
        System.out.println("3. Доставлен");
        System.out.print("Выберите новый статус: ");
        if (!scanner.hasNextLine()) {
            return;
        }
        String status = switch (scanner.nextLine().trim()) {
            case "1" -> "Принят курьером";
            case "2" -> "В пути";
            case "3" -> "Доставлен";
            default -> null;
        };
        if (status == null) {
            System.out.println("Неизвестный статус.");
            return;
        }
        delivery.updateOrderStatus(orders.get(orderNumber - 1), status);
        System.out.println("Статус заказа обновлён.");
    }

    private static int parseNumber(String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException exception) {
            return -1;
        }
    }

    private static void printMenu() {
        System.out.println("\n=== Панель администратора службы доставки ===");
        System.out.println("1. Пользователи");
        System.out.println("2. Курьеры");
        System.out.println("3. Заказы");
        System.out.println("4. Товары");
        System.out.println("5. Изменить статус заказа");
        System.out.println("0. Выход");
        System.out.print("Выберите действие: ");
    }

    private static void printUsers(List<User> users) {
        System.out.println("\nПользователи:");
        for (User user : users) {
            System.out.println(user);
        }
    }

    private static void printCouriers(Delivery delivery) {
        System.out.println("\nКурьеры:");
        List<ICourier> couriers = delivery.getCouriers();
        for (int i = 0; i < couriers.size(); i++) {
            ICourier courier = couriers.get(i);
            String availability = courier.isFree() ? "свободен" : "занят";
            System.out.println((i + 1) + ". " + courier + " | " + availability);
        }
    }

    private static void printOrders(Delivery delivery) {
        System.out.println("\nЗаказы:");
        List<IOrder> orders = delivery.getOrders();
        for (int i = 0; i < orders.size(); i++) {
            System.out.println((i + 1) + ". " + orders.get(i));
        }
    }

    private static void printProducts(Shop shop) {
        System.out.println("\nТовары:");
        List<IProduct> products = shop.getProducts();
        for (int i = 0; i < products.size(); i++) {
            System.out.println((i + 1) + ". " + products.get(i));
        }
    }

    private static void updateOrderStatus(Scanner scanner, Delivery delivery) {
        List<IOrder> orders = delivery.getOrders();
        if (orders.isEmpty()) {
            System.out.println("Заказов пока нет.");
            return;
        }

        printOrders(delivery);
        System.out.print("Введите номер заказа: ");
        if (!scanner.hasNextLine()) {
            return;
        }
        int orderNumber;
        try {
            orderNumber = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException exception) {
            System.out.println("Номер заказа должен быть целым числом.");
            return;
        }
        if (orderNumber < 1 || orderNumber > orders.size()) {
            System.out.println("Заказ с таким номером не найден.");
            return;
        }

        System.out.print("Введите новый статус: ");
        if (!scanner.hasNextLine()) {
            return;
        }
        String status = scanner.nextLine().trim();
        if (status.isEmpty()) {
            System.out.println("Статус не может быть пустым.");
            return;
        }

        delivery.updateOrderStatus(orders.get(orderNumber - 1), status);
        System.out.println("Статус заказа обновлён.");
    }

    private static List<User> createTemporaryUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User(1, "Анна Смирнова", "+7 900 111-22-33", "anna", "anna123"));
        users.add(new User(2, "Иван Петров", "+7 900 444-55-66", "ivan", "ivan123"));
        return users;
    }

    private static void seedTemporaryData(Shop shop, Delivery delivery) {
        shop.addProduct(new Product("Яблоки", 12));
        shop.addProduct(new Product("Хлеб", 7));
        delivery.addCourier(new Courier("Михаил", true, "mike", "mike123"));
        delivery.addCourier(new Courier("Елена", false, "elena", "elena123"));
        delivery.addOrder(new Order(1, "Анна Смирнова", "Яблоки", "Новый"));
        delivery.addOrder(new Order(2, "Иван Петров", "Хлеб", "В доставке"));
    }

    // Временная заглушка пользователя до появления полноценной модели и хранилища.
    private static class User {
        private final int id;
        private final String name;
        private final String phone;
        private final String login;
        private final String password;

        private User(int id, String name, String phone, String login, String password) {
            this.id = id;
            this.name = name;
            this.phone = phone;
            this.login = login;
            this.password = password;
        }

        private boolean matches(String login, String password) {
            return this.login.equals(login) && this.password.equals(password);
        }

        @Override
        public String toString() {
            return id + ". " + name + " | " + phone;
        }
    }

    // Временная заглушка курьера до появления полноценной модели и хранилища.
    private static class Courier implements ICourier {
        private final String name;
        private final boolean free;
        private final String login;
        private final String password;

        private Courier(String name, boolean free, String login, String password) {
            this.name = name;
            this.free = free;
            this.login = login;
            this.password = password;
        }

        private boolean matches(String login, String password) {
            return this.login.equals(login) && this.password.equals(password);
        }

        @Override
        public boolean isFree() {
            return free;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    // Временная заглушка товара до появления полноценной модели и хранилища.
    private static class Product implements IProduct {
        private final String name;
        private int quantity;

        private Product(String name, int quantity) {
            this.name = name;
            this.quantity = quantity;
        }

        private String getName() {
            return name;
        }

        private int getQuantity() {
            return quantity;
        }

        @Override
        public void decreaseQuantity(int amount) {
            quantity -= amount;
        }

        @Override
        public String toString() {
            return name + " (количество=" + quantity + ")";
        }
    }

    // Временная заглушка заказа до появления полноценной модели и хранилища.
    private static class Order implements IOrder {
        private final int id;
        private final String customerName;
        private final String productName;
        private String status;

        private Order(int id, String customerName, String productName, String status) {
            this.id = id;
            this.customerName = customerName;
            this.productName = productName;
            this.status = status;
        }

        private boolean belongsTo(User user) {
            return customerName.equals(user.name);
        }

        @Override
        public void setStatus(String status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return "Заказ " + id + " | клиент: " + customerName + " | товар: " + productName + " | статус: " + status;
        }
    }
}
