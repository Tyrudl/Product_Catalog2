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
                case 4 -> showCacheStats();
                case 5 -> showMetrics();
                case 6 -> {
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

            switch (searchChoice) {
                case 1 -> searchById();
                case 2 -> searchByName();
                case 3 -> searchByCategory();
                case 4 -> searchByBrand();
                case 5 -> searchByPriceRange();
                case 6 -> { return; }
                default -> menu.showMessage("Неверный выбор!");
            }
        }
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

    private static void showAllProducts() {
        List<Product> allProducts = productService.getAllProducts();
        menu.displayProducts(allProducts);
    }

    private static void showCacheStats() {
        productService.showCacheStats();
    }

    private static void showMetrics() {
        productService.showMetrics();
    }
}