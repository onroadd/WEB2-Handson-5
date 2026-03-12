package com.example.springmvclab.controller;

import com.example.springmvclab.model.Product;
import com.example.springmvclab.service.ProductService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/products")
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String listProducts(Model model) {
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        model.addAttribute("title", "Daftar Produk");
        model.addAttribute("totalProducts", products.size());
        model.addAttribute("keyword", "");
        return "product/list";
    }

    @GetMapping("/{id}")
    public String productDetail(@PathVariable Long id, Model model) {
        Optional<Product> product = productService.findById(id);

        if (product.isPresent()) {
            model.addAttribute("product", product.get());
            model.addAttribute("title", "Detail: " + product.get().getName());
        } else {
            model.addAttribute("error", "Produk dengan ID " + id + " tidak ditemukan");
            model.addAttribute("title", "Produk Tidak Ditemukan");
        }

        return "product/detail";
    }

    @GetMapping("/category/{category}")
    public String productsByCategory(@PathVariable String category, Model model) {
        List<Product> products = productService.findByCategory(category);
        model.addAttribute("products", products);
        model.addAttribute("title", "Kategori: " + category);
        model.addAttribute("totalProducts", products.size());
        model.addAttribute("selectedCategory", category);
        model.addAttribute("keyword", "");
        return "product/list";
    }

    @GetMapping("/search")
    public String searchProducts(@RequestParam(defaultValue = "") String keyword,
                                 Model model) {
        List<Product> products = keyword.isBlank()
                ? productService.findAll()
                : productService.search(keyword);

        model.addAttribute("products", products);
        model.addAttribute("title", "Hasil Pencarian: " + keyword);
        model.addAttribute("totalProducts", products.size());
        model.addAttribute("keyword", keyword);
        return "product/list";
    }

    @GetMapping("/categories")
    public String categories(Model model) {
        List<String> categories = productService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("title", "Daftar Kategori");
        model.addAttribute("productService", productService);
        return "product/categories";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", productService.getAllCategories());
        model.addAttribute("title", "Tambah Produk");
        return "product/create";
    }

    @PostMapping("/new")
    public String createProduct(@Valid @ModelAttribute Product product,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.warn("Gagal tambah produk karena validasi tidak lolos. name={}, category={}, price={}, stock={}",
                    product.getName(), product.getCategory(), product.getPrice(), product.getStock());
            model.addAttribute("categories", productService.getAllCategories());
            model.addAttribute("title", "Tambah Produk");
            return "product/create";
        }
        productService.addProduct(product);
        log.info("Produk ditambahkan: id={}, name={}, category={}, price={}, stock={}",
                product.getId(), product.getName(), product.getCategory(), product.getPrice(), product.getStock());
        redirectAttributes.addFlashAttribute("successMessage", "Product berhasil ditambahkan");
        return "redirect:/products";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Product> product = productService.findById(id);
        if (product.isEmpty()) {
            redirectAttributes.addFlashAttribute("successMessage", "Produk tidak ditemukan");
            return "redirect:/products";
        }
        model.addAttribute("product", product.get());
        model.addAttribute("categories", productService.getAllCategories());
        model.addAttribute("title", "Edit Produk");
        return "product/edit";
    }

    @PostMapping("/{id}/edit")
    public String updateProduct(@PathVariable Long id,
                                @Valid @ModelAttribute Product product,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        product.setId(id);
        if (bindingResult.hasErrors()) {
            log.warn("Gagal edit produk karena validasi tidak lolos. id={}, name={}, category={}, price={}, stock={}",
                    id, product.getName(), product.getCategory(), product.getPrice(), product.getStock());
            model.addAttribute("categories", productService.getAllCategories());
            model.addAttribute("title", "Edit Produk");
            return "product/edit";
        }
        boolean updated = productService.updateProduct(product);
        if (!updated) {
            log.warn("Gagal edit produk karena id tidak ditemukan. id={}", id);
            redirectAttributes.addFlashAttribute("successMessage", "Produk tidak ditemukan");
            return "redirect:/products";
        }
        log.info("Produk diedit: id={}, name={}, category={}, price={}, stock={}",
                product.getId(), product.getName(), product.getCategory(), product.getPrice(), product.getStock());
        redirectAttributes.addFlashAttribute("successMessage", "Product berhasil diedit");
        return "redirect:/products";
    }
}
