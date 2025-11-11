package util;

import model.Product;
import java.util.List;
import java.util.Scanner;

public class ConsoleMenu {
    private Scanner scanner = new Scanner(System.in);

    public void showLoginMenu() {
        System.out.println("\n=== АВТОРИЗАЦИЯ ===");
        System.out.println("1. Войти в систему");
        System.out.println("2. Продолжить как гость");
        System.out.println("3. Выйти из программы");
        System.out.print("Выберите действие: ");
    }

    public void showMainMenu(String username) {
        System.out.println("\n=== КАТАЛОГ ТОВАРОВ ===");
        System.out.println("👤 Пользователь: " + username);
        System.out.println("1. Добавить товар");
        System.out.println("2. Поиск товаров");
        System.out.println("3. Показать все товары");
        System.out.println("4. Статистика кэша");
        System.out.println("5. Метрики системы");
        System.out.println("6. Выйти из системы");
        System.out.print("Выберите действие: ");
    }

    public void showSearchMenu() {
        System.out.println("\n=== ПОИСК ТОВАРОВ ===");
        System.out.println("1. Поиск по ID");
        System.out.println("2. Поиск по названию");
        System.out.println("3. Поиск по категории");
        System.out.println("4. Поиск по бренду");
        System.out.println("5. Поиск по диапазону цен");
        System.out.println("6. Назад в главное меню");
        System.out.print("Выберите тип поиска: ");
    }

    public String askUsername() {
        System.out.print("Введите логин: ");
        return scanner.nextLine();
    }

    public String askPassword() {
        System.out.print("Введите пароль: ");
        return scanner.nextLine();
    }

    public int getChoice() {
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
    }

    public String askString(String question) {
        System.out.print(question + ": ");
        return scanner.nextLine();
    }

    public double askDouble(String question) {
        while (true) {
            System.out.print(question + ": ");
            String input = scanner.nextLine();
            try {
                input = input.replace(',', '.');
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка! Введите число (например: 999.99 или 999,99)");
            }
        }
    }

    public void showMessage(String message) {
        System.out.println(message);
    }

    public void displayProducts(List<Product> products) {
        if (products.isEmpty()) {
            System.out.println("Товары не найдены");
            return;
        }

        System.out.println("\n=== НАЙДЕНО ТОВАРОВ: " + products.size() + " ===");
        for (Product product : products) {
            System.out.println("📦 " + product.getName() +
                    " | 💰 $" + product.getPrice() +
                    " | 🏷 " + product.getBrand() +
                    " | 📁 " + product.getCategory() +
                    " | #" + product.getId());
        }
    }
}