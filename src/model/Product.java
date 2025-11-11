package model;

import java.io.Serializable;

public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String category;
    private String brand;
    private double price;

    public Product(String id, String name, String category, String brand, double price) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.brand = brand;
        this.price = price;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getBrand() { return brand; }
    public double getPrice() { return price; }

    @Override
    public String toString() {
        return String.format("Product{id='%s', name='%s', category='%s', brand='%s', price=%.2f}",
                id, name, category, brand, price);
    }
}