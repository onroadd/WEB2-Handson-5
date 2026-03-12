package com.example.springmvclab.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class Product {
    private Long id;

    @NotBlank(message = "Nama produk wajib diisi.")
    @Size(min = 3, max = 80, message = "Nama produk minimal 3 dan maksimal 80 karakter.")
    private String name;

    @NotBlank(message = "Kategori wajib dipilih.")
    private String category;

    @NotBlank(message = "Deskripsi wajib diisi.")
    @Size(min = 10, max = 300, message = "Deskripsi minimal 10 dan maksimal 300 karakter.")
    private String description;

    @NotNull(message = "Harga wajib diisi.")
    @Positive(message = "Harga harus lebih dari 0.")
    private Double price;

    @Min(value = 0, message = "Stok tidak boleh kurang dari 0.")
    private int stock;

    public Product() {
    }

    public Product(Long id, String name, String category, String description, Double price, int stock) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.price = price;
        this.stock = stock;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
