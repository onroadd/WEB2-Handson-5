package com.example.springmvclab.service;

import com.example.springmvclab.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final List<Product> products = new ArrayList<>();

    public ProductService() {
        products.add(new Product(1L, "Laptop ASUS", "Elektronik",
                "Laptop performa tinggi untuk kebutuhan kerja dan hiburan.", 12_500_000d, 15));
        products.add(new Product(2L, "Mouse Logitech", "Elektronik",
                "Mouse ergonomis dengan sensor presisi untuk produktivitas.", 350_000d, 50));
        products.add(new Product(3L, "Buku Java Programming", "Buku",
                "Panduan belajar Java dari dasar hingga OOP.", 150_000d, 30));
        products.add(new Product(4L, "Kopi Arabica 250g", "Makanan",
                "Kopi arabica single origin dengan aroma floral.", 85_000d, 100));
        products.add(new Product(5L, "Headphone Sony", "Elektronik",
                "Headphone dengan bass kuat dan noise isolation.", 1_200_000d, 20));
        products.add(new Product(6L, "Novel Laskar Pelangi", "Buku",
                "Novel inspiratif tentang mimpi dan persahabatan.", 75_000d, 45));
    }

    public List<Product> findAll() {
        return products;
    }

    public Optional<Product> findById(Long id) {
        return products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    public List<Product> findByCategory(String category) {
        return products.stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .toList();
    }

    public List<Product> search(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return products.stream()
                .filter(p -> p.getName().toLowerCase().contains(lowerKeyword)
                        || matchesId(p, lowerKeyword))
                .toList();
    }

    private boolean matchesId(Product product, String keyword) {
        if (!keyword.chars().allMatch(Character::isDigit)) {
            return false;
        }
        try {
            long id = Long.parseLong(keyword);
            return product.getId() != null && product.getId() == id;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public List<String> getAllCategories() {
        return products.stream()
                .map(Product::getCategory)
                .distinct()
                .sorted()
                .toList();
    }

    public long countByCategory(String category) {
        return products.stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .count();
    }

    public Optional<Product> findMostExpensive() {
        return products.stream()
                .max((a, b) -> Double.compare(a.getPrice(), b.getPrice()));
    }

    public Optional<Product> findCheapest() {
        return products.stream()
                .min((a, b) -> Double.compare(a.getPrice(), b.getPrice()));
    }

    public double getAveragePrice() {
        return products.stream()
                .mapToDouble(Product::getPrice)
                .average()
                .orElse(0);
    }

    public long countLowStock() {
        return products.stream()
                .filter(p -> p.getStock() < 20)
                .count();
    }

    public void addProduct(Product product) {
        product.setId(nextId());
        products.add(product);
        log.info("PRODUCT_ADDED id={} name='{}' category='{}' price={} stock={}",
                product.getId(), product.getName(), product.getCategory(),
                product.getPrice(), product.getStock());
    }

    public boolean updateProduct(Product updated) {
        for (Product product : products) {
            if (product.getId().equals(updated.getId())) {
                product.setName(updated.getName());
                product.setCategory(updated.getCategory());
                product.setDescription(updated.getDescription());
                product.setPrice(updated.getPrice());
                product.setStock(updated.getStock());
                log.info("PRODUCT_UPDATED id={} name='{}' category='{}' price={} stock={}",
                        product.getId(), product.getName(), product.getCategory(),
                        product.getPrice(), product.getStock());
                return true;
            }
        }
        log.warn("PRODUCT_UPDATE_FAILED id={} not_found", updated.getId());
        return false;
    }

    private Long nextId() {
        return products.stream()
                .map(Product::getId)
                .max(Long::compareTo)
                .orElse(0L) + 1;
    }
}
