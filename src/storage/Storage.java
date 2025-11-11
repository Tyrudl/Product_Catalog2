package storage;

import model.Product;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private static final String PRODUCTS_FILE = "products.dat";
    private static final String NEXT_ID_FILE = "next_id.dat";

    public void saveProducts(List<Product> products) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(PRODUCTS_FILE))) {
            oos.writeObject(products);
            System.out.println("💾 Товары сохранены в файл: " + PRODUCTS_FILE);
        } catch (IOException e) {
            System.out.println("❌ Ошибка сохранения товаров: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public List<Product> loadProducts() {
        File file = new File(PRODUCTS_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(PRODUCTS_FILE))) {
            List<Product> products = (List<Product>) ois.readObject();
            System.out.println("📂 Товары загружены из файла: " + products.size() + " товаров");
            return products;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("❌ Ошибка загрузки товаров: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void saveNextId(int nextId) {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(NEXT_ID_FILE))) {
            dos.writeInt(nextId);
        } catch (IOException e) {
            System.out.println("❌ Ошибка сохранения nextId: " + e.getMessage());
        }
    }

    public int loadNextId() {
        File file = new File(NEXT_ID_FILE);
        if (!file.exists()) {
            return 1;
        }

        try (DataInputStream dis = new DataInputStream(new FileInputStream(NEXT_ID_FILE))) {
            return dis.readInt();
        } catch (IOException e) {
            System.out.println("❌ Ошибка загрузки nextId: " + e.getMessage());
            return 1;
        }
    }
}