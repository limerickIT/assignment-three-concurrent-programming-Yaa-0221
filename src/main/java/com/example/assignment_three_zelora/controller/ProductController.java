package com.example.assignment_three_zelora.controller;

import com.example.assignment_three_zelora.model.entitys.Inventory;
import com.example.assignment_three_zelora.model.entitys.Product;
import com.example.assignment_three_zelora.model.entitys.Review;
import com.example.assignment_three_zelora.model.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "index";
    }


    @GetMapping("/products")
    public String list(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "index";
    }

    @GetMapping("/products/{id}")
    public String detail(@PathVariable Integer id, Model model) {

        Product product = productService.getProductById(id);

        // 1) 计算平均评分（只算 rating >= 3 的）
        List<Review> allReviews = product.getReviewList();
        List<Review> filteredReviews = new ArrayList<>();
        double avgRating = 0.0;

        if (allReviews != null && !allReviews.isEmpty()) {
            int sum = 0;
            int count = 0;
            for (Review r : allReviews) {
                if (r.getRating() != null && r.getRating() >= 3) {
                    filteredReviews.add(r);
                    sum += r.getRating();
                    count++;
                }
            }
            if (count > 0) {
                avgRating = (double) sum / count;
            }
        }

        // 2) 计算库存：quantity_in_stock - quantity_reserved
        // 假设 Inventory 有 getQuantityInStock() / getQuantityReserved()
        int totalAvailable = 0;
        if (product.getInventoryList() != null) {
            for (Inventory inv : product.getInventoryList()) {
                Integer inStock = inv.getQuantityInStock();
                Integer reserved = inv.getQuantityReserved();
                if (inStock != null && reserved != null) {
                    totalAvailable += (inStock - reserved);
                }
            }
        }

        String stockStatus;
        if (totalAvailable <= 0) {
            stockStatus = "Out of Stock";
        } else if (totalAvailable < 10) { // 阈值你可以根据作业要求调整
            stockStatus = "Low Stock";
        } else {
            stockStatus = "In Stock";
        }

        model.addAttribute("product", product);
        model.addAttribute("reviews", filteredReviews); // 只包含 rating >= 3 的
        model.addAttribute("avgRating", avgRating);
        model.addAttribute("availableQuantity", totalAvailable);
        model.addAttribute("stockStatus", stockStatus);

        return "product-detail";
    }
}
