package ru.leti.lab1;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
        while (true) {
            printLoginMenu();
            if (!scanner.hasNextLine()) {
                return;
            }
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
        Map<Integer, Integer> cart = new LinkedHashMap<>();
        while (true) {
            printProductsAndCart(shop, cart);
            System.out.print("Введите номер товара и количество (например, 1 3; количество по умолчанию 1; 0 - оформить): ");
            if (!scanner.hasNextLine()) {
                return;
            }
            String[] selection = scanner.nextLine().trim().split("\\s+");
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

            Product product = (Product) products.get(productNumber - 1);
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
        List<String> orderProducts = new ArrayList<>();
        long totalPrice = 0;
        for (Map.Entry<Integer, Integer> item : cart.entrySet()) {
            Product product = (Product) products.get(item.getKey());
            for (int i = 0; i < item.getValue(); i++) {
                shop.addProduct2basket(item.getKey());
            }
            orderProducts.add(product.getName() + " x " + item.getValue());
            totalPrice += (long) product.getPrice() * item.getValue();
        }
        int orderId = delivery.getOrders().size() + 1;
        Order order = new Order(orderId, user.name, String.join(", ", orderProducts), "Новый", totalPrice);
        delivery.addOrder(order);
        System.out.println("Заказ " + orderId + " оформлен. Сумма: " + totalPrice + " руб.");
        System.out.println("Заказ ожидает курьера.");
    }

    private static void printProductsAndCart(Shop shop, Map<Integer, Integer> cart) {
        List<IProduct> products = shop.getProducts();
        List<Map.Entry<Integer, Integer>> cartItems = new ArrayList<>(cart.entrySet());
        System.out.printf("%-48s | %s%n", "Товары", "Текущая корзина");
        System.out.println("-".repeat(48) + "+" + "-".repeat(32));
        int rowCount = Math.max(products.size(), cartItems.size());
        for (int row = 0; row < rowCount; row++) {
            String productText = "";
            String cartText = "";
            if (row < products.size()) {
                Product product = (Product) products.get(row);
                int remaining = product.getQuantity() - cart.getOrDefault(row, 0);
                productText = (row + 1) + ". " + product.getName() + " | " + product.getPrice()
                    + " руб./шт. | доступно: " + remaining;
            }
            if (row < cartItems.size()) {
                Map.Entry<Integer, Integer> item = cartItems.get(row);
                Product product = (Product) products.get(item.getKey());
                cartText = product.getName() + " x " + item.getValue();
            }
            System.out.printf("%-48s | %s%n", productText, cartText);
        }
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
            System.out.println("2. Взять новый заказ в работу");
            System.out.println("3. Изменить статус");
            System.out.println("0. Выйти из аккаунта");
            System.out.print("Выберите действие: ");
            if (!scanner.hasNextLine()) {
                return;
            }
            switch (scanner.nextLine().trim()) {
                case "1" -> printCourierOrders(courier, delivery);
                case "2" -> takeNewOrder(scanner, courier, delivery);
                case "3" -> updateCourierStatusMenu(scanner, courier, delivery);
                case "0" -> running = false;
                default -> System.out.println("Неизвестный пункт меню.");
            }
        }
    }

    private static void updateCourierStatusMenu(Scanner scanner, Courier courier, Delivery delivery) {
        System.out.println("\n1. Изменить статус заказа");
        System.out.println("2. Изменить статус курьера");
        System.out.println("0. Назад");
        System.out.print("Выберите действие: ");
        if (!scanner.hasNextLine()) {
            return;
        }
        switch (scanner.nextLine().trim()) {
            case "1" -> updateCourierOrderStatus(scanner, courier, delivery);
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
        List<IOrder> orders = delivery.getAvailableNewOrders();
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
        String statusInput = scanner.nextLine().trim();
        boolean newStatus;
        switch (statusInput) {
            case "1" -> newStatus = true;
            case "2" -> newStatus = false;
            default -> {
                System.out.println("Неизвестный статус.");
                return;
            }
        }
        if (newStatus && delivery.hasActiveOrdersForCourier(courier)) {
            System.out.println("Нельзя стать свободным, пока есть незавершённая доставка.");
            return;
        }
        courier.setFree(newStatus);
        System.out.println("Статус курьера обновлён.");
    }

    private static void printCourierOrders(Courier courier, Delivery delivery) {
        List<IOrder> orders = delivery.getOrdersForCourier(courier);
        System.out.println("\nВаши заказы:");
        if (orders.isEmpty()) {
            System.out.println("Заказов, назначенных вам, пока нет.");
            return;
        }
        for (int i = 0; i < orders.size(); i++) {
            System.out.println((i + 1) + ". " + orders.get(i));
        }
    }

    private static void updateCourierOrderStatus(Scanner scanner, Courier courier, Delivery delivery) {
        List<IOrder> orders = delivery.getOrdersForCourier(courier);
        if (orders.isEmpty()) {
            System.out.println("У вас нет назначенных заказов.");
            return;
        }
        printCourierOrders(courier, delivery);
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
        shop.addProduct(new Product("Яблоки", 12, 120));
        shop.addProduct(new Product("Хлеб", 7, 50));
        Courier mike = new Courier("Михаил", true, "mike", "mike123");
        Courier elena = new Courier("Елена", false, "elena", "elena123");
        delivery.addCourier(mike);
        delivery.addCourier(elena);
        delivery.addOrder(new Order(1, "Анна Смирнова", "Яблоки x 1", "Новый", 120));
        Order inDelivery = new Order(2, "Иван Петров", "Хлеб x 1", "В доставке", 50);
        inDelivery.assignCourier(elena);
        delivery.addOrder(inDelivery);
    }

}
