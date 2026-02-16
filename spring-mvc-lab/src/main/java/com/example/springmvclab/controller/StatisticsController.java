package com.example.springmvclab.controller;

import com.example.springmvclab.model.Product;
import com.example.springmvclab.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class StatisticsController {

    private final ProductService productService;

    public StatisticsController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/statistics")
    public String statistics(Model model) {
        List<Product> products = productService.findAll();

        Map<String, Integer> productCountByCategory = new LinkedHashMap<>();
        for (String category : productService.getAllCategories()) {
            productCountByCategory.put(category, (int) productService.countByCategory(category));
        }

        model.addAttribute("totalProducts", products.size());
        model.addAttribute("productCountByCategory", productCountByCategory);
        model.addAttribute("mostExpensive", productService.findMostExpensive().orElse(null));
        model.addAttribute("cheapest", productService.findCheapest().orElse(null));
        model.addAttribute("averagePrice", productService.getAveragePrice());
        model.addAttribute("lowStockCount", productService.countLowStock());
        model.addAttribute("title", "Statistik");
        return "statistics";
    }
}
