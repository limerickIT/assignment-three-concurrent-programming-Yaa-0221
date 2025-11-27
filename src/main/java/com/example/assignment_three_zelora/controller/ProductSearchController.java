package com.example.assignment_three_zelora.controller;


import com.example.assignment_three_zelora.model.entitys.Product;
import com.example.assignment_three_zelora.model.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductSearchController {

    private final ProductService productService;

    public ProductSearchController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/search")
    public String searchPage() {
        return "product-search";
    }

    @GetMapping("/search/results")
    public String searchResults(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sort,
            Model model) {

        List<Product> results = productService.searchProducts(name, category, minPrice, maxPrice, keyword, sort);
        model.addAttribute("products", results);

        return "product-search-results";
    }
}

