import auth.AuthService;
import auth.AuditService;
import model.Product;
import service.ProductService;
import util.ConsoleMenu;
import java.util.List;

public class Main {
    private static ConsoleMenu menu = new ConsoleMenu();
    private static AuditService auditService = new AuditService();
    private static AuthService authService = new AuthService(auditService);
    private static ProductService productService = new ProductService(auditService);

    public static void main(String[] args) {
        addSampleData();
        menu.showMessage("=== ДОБРО ПОЖАЛОВАТЬ В КАТАЛОГ ТОВАРОВ ===");

        while (true) {
            menu.showLoginMenu();
            int choice = menu.getChoice();

            switch (choice) {
                case 1 -> handleLogin();
                case 2 -> handleGuestMode();
                case 3 -> {
                    menu.showMessage("Выход из программы...");
                    return;
                }
                default -> menu.showMessage("Неверный выбор!");
            }
        }
    }

    private static void addSampleData() {
        if (productService.getAllProducts().isEmpty()) {
            productService.addProduct("iPhone 15", "Electronics", "Apple", 999.99, "system");
            productService.addProduct("MacBook Pro", "Electronics", "Apple", 1999.99, "system");
            productService.addProduct("Galaxy S24", "Electronics", "Samsung", 899.99, "system");
            productService.addProduct("Nike Air Max", "Shoes", "Nike", 129.99, "system");
            productService.addProduct("Adidas Ultraboost", "Shoes", "Adidas", 179.99, "system");
        }
    }

    private static void handleLogin() {
        String username = menu.askUsername();
        String password = menu.askPassword();

        if (authService.login(username, password)) {
            menu.showMessage("Успешный вход! Добро пожаловать, " + username + "!");
            mainMenuLoop();
        } else {
            menu.showMessage("Неверный логин или пароль!");
        }
    }

    private static void handleGuestMode() {
        menu.showMessage("Вы вошли как гость (доступен только просмотр)");
        authService.login("guest", "");
        mainMenuLoop();
    }

    private static void mainMenuLoop() {
        while (authService.isLoggedIn()) {
            menu.showMainMenu(authService.getCurrentUsername());
            int choice = menu.getChoice();

            switch (choice) {
                case 1 -> addNewProduct();
                case 2 -> searchProducts();
                case 3 -> showAllProducts();
                case 4 -> deleteProduct();
                case 5 -> showProductStatistics();
                case 6 -> showCacheStats();
                case 7 -> showMetrics();
                case 8 -> {
                    authService.logout();
                    menu.showMessage("Вы вышли из системы");
                    return;
                }
                default -> menu.showMessage("Неверный выбор!");
            }
        }
    }

    private static void addNewProduct() {
        if (authService.getCurrentUsername().equals("guest")) {
            menu.showMessage("Гости не могут добавлять товары!");
            return;
        }

        menu.showMessage("\n=== ДОБАВЛЕНИЕ НОВОГО ТОВАРА ===");
        String name = menu.askString("Введите название товара");
        String category = menu.askString("Введите категорию");
        String brand = menu.askString("Введите бренд");
        double price = menu.askDouble("Введите цену");

        productService.addProduct(name, category, brand, price, authService.getCurrentUsername());
        menu.showMessage("Товар успешно добавлен!");
    }

    private static void searchProducts() {
        while (authService.isLoggedIn()) {
            menu.showSearchMenu();
            int searchChoice = menu.getChoice();

            switch (searchChoice) { // ИСПРАВЛЕНО: было switch (choice)
                case 1 -> addNewProduct();
                case 2 -> searchProducts();
                case 3 -> showAllProducts();
                case 4 -> deleteProduct();
                case 5 -> showProductStatistics();
                case 6 -> showCacheStats();
                case 7 -> showMetrics();
                case 8 -> {
                    return; // выход из меню поиска
                }
                default -> menu.showMessage("Неверный выбор!");
            }
        }
    }

    // ДОБАВЛЕН отсутствующий метод showAllProducts()
    private static void showAllProducts() {
        List<Product> allProducts = productService.getAllProducts();
        menu.displayProducts(allProducts);
    }

    // ДОБАВЛЕН отсутствующий метод showProductStatistics()
    private static void showProductStatistics() {
        // Реализация показа статистики товаров
        menu.showMessage("\n=== СТАТИСТИКА ТОВАРОВ ===");
        // Здесь можно добавить логику для показа статистики
        List<Product> allProducts = productService.getAllProducts();
        menu.showMessage("Общее количество товаров: " + allProducts.size());

        // Пример простой статистики
        double totalValue = allProducts.stream()
                .mapToDouble(Product::getPrice)
                .sum();
        menu.showMessage("Общая стоимость всех товаров: $" + String.format("%.2f", totalValue));
    }

    private static void searchById() {
        String id = menu.askString("Введите ID товара");
        Product product = productService.findProductById(id);
        if (product != null) {
            menu.displayProducts(List.of(product));
        } else {
            menu.showMessage("Товар с ID " + id + " не найден");
        }
    }

    private static void searchByName() {
        String name = menu.askString("Введите название товара (или часть)");
        List<Product> products = productService.findProductsByName(name);
        menu.displayProducts(products);
    }

    private static void searchByCategory() {
        String category = menu.askString("Введите категорию");
        List<Product> products = productService.findProductsByCategory(category);
        menu.displayProducts(products);
    }

    private static void searchByBrand() {
        String brand = menu.askString("Введите бренд");
        List<Product> products = productService.findProductsByBrand(brand);
        menu.displayProducts(products);
    }

    private static void searchByPriceRange() {
        double minPrice = menu.askDouble("Введите минимальную цену");
        double maxPrice = menu.askDouble("Введите максимальную цену");
        List<Product> products = productService.findProductsByPriceRange(minPrice, maxPrice);
        menu.displayProducts(products);
    }

    private static void showAllProductsWithIds() {
        List<Product> allProducts = productService.getAllProducts();
        menu.showMessage("\n=== ВСЕ ТОВАРЫ С ID ===");
        for (Product product : allProducts) {
            menu.showMessage("ID: " + product.getId() + " | " + product.getName() + " | $" + product.getPrice());
        }
    }

    private static void showCacheStats() {
        productService.showCacheStats();
    }

    private static void showMetrics() {
        productService.showMetrics();
    }

    private static void deleteProduct() {
        if (authService.getCurrentUsername().equals("guest")) {
            menu.showMessage("❌ Гости не могут удалять товары!");
            return;
        }

        // Сначала покажем все товары с ID
        showAllProductsWithIds();

        menu.showMessage("\n=== УДАЛЕНИЕ ТОВАРА ===");
        String id = menu.askString("Введите ID товара для удаления");

        // Проверяем существует ли товар
        Product productToDelete = productService.findProductById(id);
        if (productToDelete == null) {
            menu.showMessage("❌ Товар с ID " + id + " не найден");
            return;
        }

        // Подтверждение удаления
        menu.showMessage("Вы собираетесь удалить: " + productToDelete.getName() + " за $" + productToDelete.getPrice());
        String confirm = menu.askString("Подтвердите удаление (введите 'yes' для подтверждения)");

        if ("yes".equalsIgnoreCase(confirm)) {
            boolean deleted = productService.deleteProduct(id, authService.getCurrentUsername());
            if (deleted) {
                menu.showMessage("✅ Товар успешно удален!");
            } else {
                menu.showMessage("❌ Ошибка при удалении товара");
            }
        } else {
            menu.showMessage("❌ Удаление отменено");
        }
    }
}