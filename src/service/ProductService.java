package service;

import auth.AuditService;
import model.Product;
import storage.ProductStorage;
import storage.Storage;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;

public class ProductService {
    private ProductStorage storage = new ProductStorage();
    private AuditService auditService;
    private SearchCache searchCache = new SearchCache();
    private MetricsService metricsService = new MetricsService();
    private int nextId;

    public ProductService(AuditService auditService) {
        this.auditService = auditService;
        this.nextId = new Storage().loadNextId();
    }

    public void addProduct(String name, String category, String brand, double price, String username) {
        String id = String.valueOf(nextId++);
        Product product = new Product(id, name, category, brand, price);
        storage.addProduct(product);

        new Storage().saveNextId(nextId);
        searchCache.clear();

        auditService.logProductAction(username, "ДОБАВЛЕН товар",
                String.format("ID: %s, Название: %s, Цена: $%.2f", id, name, price));
    }

    public Product findProductById(String id) {
        long startTime = System.currentTimeMillis();

        String cacheKey = "id_" + id;
        List<Product> cached = searchCache.get(cacheKey);

        Product result;
        if (cached != null) {
            result = cached.isEmpty() ? null : cached.get(0);
            System.out.println("⚡ Результат из КЭША");
        } else {
            result = storage.findById(id);
            searchCache.put(cacheKey, result != null ? List.of(result) : List.of());
        }

        long duration = System.currentTimeMillis() - startTime;
        metricsService.recordSearch("Поиск по ID", duration, result != null ? 1 : 0);
        return result;
    }

    public List<Product> findProductsByName(String name) {
        long startTime = System.currentTimeMillis();
        String cacheKey = "name_" + name.toLowerCase();
        List<Product> result = searchCache.get(cacheKey);

        if (result == null) {
            result = storage.findByName(name);
            searchCache.put(cacheKey, result);
        } else {
            System.out.println("⚡ Результат из КЭША");
        }

        long duration = System.currentTimeMillis() - startTime;
        metricsService.recordSearch("Поиск по названию", duration, result.size());
        return result;
    }

    public List<Product> findProductsByCategory(String category) {
        long startTime = System.currentTimeMillis();
        String cacheKey = "category_" + category.toLowerCase();
        List<Product> result = searchCache.get(cacheKey);

        if (result == null) {
            result = storage.findByCategory(category);
            searchCache.put(cacheKey, result);
        } else {
            System.out.println("⚡ Результат из КЭША");
        }

        long duration = System.currentTimeMillis() - startTime;
        metricsService.recordSearch("Поиск по категории", duration, result.size());
        return result;
    }

    public List<Product> findProductsByBrand(String brand) {
        long startTime = System.currentTimeMillis();
        String cacheKey = "brand_" + brand.toLowerCase();
        List<Product> result = searchCache.get(cacheKey);

        if (result == null) {
            result = storage.findByBrand(brand);
            searchCache.put(cacheKey, result);
        } else {
            System.out.println("⚡ Результат из КЭША");
        }

        long duration = System.currentTimeMillis() - startTime;
        metricsService.recordSearch("Поиск по бренду", duration, result.size());
        return result;
    }

    public List<Product> findProductsByPriceRange(double minPrice, double maxPrice) {
        long startTime = System.currentTimeMillis();
        String cacheKey = String.format("price_%.2f_%.2f", minPrice, maxPrice);
        List<Product> result = searchCache.get(cacheKey);

        if (result == null) {
            result = storage.findByPriceRange(minPrice, maxPrice);
            searchCache.put(cacheKey, result);
        } else {
            System.out.println("⚡ Результат из КЭША");
        }

        long duration = System.currentTimeMillis() - startTime;
        metricsService.recordSearch("Поиск по цене", duration, result.size());
        return result;
    }

    public List<Product> getAllProducts() {
        long startTime = System.currentTimeMillis();
        String cacheKey = "all_products";
        List<Product> result = searchCache.get(cacheKey);

        if (result == null) {
            result = storage.getAllProducts();
            searchCache.put(cacheKey, result);
        } else {
            System.out.println("⚡ Результат из КЭША");
        }

        long duration = System.currentTimeMillis() - startTime;
        metricsService.recordSearch("Все товары", duration, result.size());
        return result;
    }

    public void showCacheStats() {
        System.out.println("\n=== СТАТИСТИКА КЭША ===");
        System.out.println("📦 Размер кэша: " + searchCache.getSize() + " записей");
        System.out.println("📊 Всего запросов: " + metricsService.getTotalSearchCount());
        System.out.println("=== КОНЕЦ СТАТИСТИКИ ===");
    }

    public void showMetrics() {
        metricsService.showMetrics();
    }
}