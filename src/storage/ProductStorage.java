package storage;

import model.Product;
import java.util.*;

public class ProductStorage {
    private Map<String, Product> products = new HashMap<>();
    private List<Product> allProducts = new ArrayList<>();
    private Storage fileStorage;

    public ProductStorage() {
        this.fileStorage = new Storage();
        loadFromFile();
    }

    private void loadFromFile() {
        List<Product> loadedProducts = fileStorage.loadProducts();
        for (Product product : loadedProducts) {
            products.put(product.getId(), product);
            allProducts.add(product);
        }
    }

    public void addProduct(Product product) {
        products.put(product.getId(), product);
        allProducts.add(product);
        saveToFile();
    }

    public Product findById(String id) {
        return products.get(id);
    }

    public List<Product> findByName(String name) {
        List<Product> result = new ArrayList<>();
        for (Product product : allProducts) {
            if (product.getName().toLowerCase().contains(name.toLowerCase())) {
                result.add(product);
            }
        }
        return result;
    }

    public List<Product> findByCategory(String category) {
        List<Product> result = new ArrayList<>();
        for (Product product : allProducts) {
            if (product.getCategory().equalsIgnoreCase(category)) {
                result.add(product);
            }
        }
        return result;
    }

    public List<Product> findByBrand(String brand) {
        List<Product> result = new ArrayList<>();
        for (Product product : allProducts) {
            if (product.getBrand().equalsIgnoreCase(brand)) {
                result.add(product);
            }
        }
        return result;
    }

    public List<Product> findByPriceRange(double minPrice, double maxPrice) {
        List<Product> result = new ArrayList<>();
        for (Product product : allProducts) {
            if (product.getPrice() >= minPrice && product.getPrice() <= maxPrice) {
                result.add(product);
            }
        }
        return result;
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(allProducts);
    }

    public void saveToFile() {
        fileStorage.saveProducts(allProducts);
    }

    public int getProductCount() {
        return products.size();
    }

    public boolean deleteProduct(String id) {
        // Проверяем существует ли товар
        if (!products.containsKey(id)) {
            return false;
        }

        // Удаляем из обеих структур
        Product removed = products.remove(id);
        boolean removedFromList = allProducts.removeIf(product -> product.getId().equals(id));

        if (removed != null && removedFromList) {
            saveToFile(); // Сохраняем изменения
            System.out.println("✅ Удален товар: " + removed.getName());
            return true;
        }

        return false;
    }

}