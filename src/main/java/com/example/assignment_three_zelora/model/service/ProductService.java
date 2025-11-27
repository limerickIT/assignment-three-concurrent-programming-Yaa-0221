package com.example.assignment_three_zelora.model.service;

import com.example.assignment_three_zelora.model.entitys.Product;
import com.example.assignment_three_zelora.model.repos.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    //create
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    //Get all
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    //get one
    public Product getProductById(Integer id) {
        return productRepository.findById(id).orElse(null);
    }

    //Update
    public Product updateProduct(Integer id, Product updatedProduct) {
        if (!productRepository.existsById(id)) {
            return null;
        }
        updatedProduct.setProductId(id);
        return productRepository.save(updatedProduct);
    }
    public List<Product> searchProducts(
            String name,
            String category,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            String keyword,
            String sort) {

        // 1) Name search
        if (name != null && !name.isBlank()) {
            return productRepository.findByProductNameContainingIgnoreCase(name);
        }

        // 2) Category search
        if (category != null && !category.isBlank()) {
            return productRepository.findByCategoryId_CategoryNameContainingIgnoreCase(category);
        }

        // 3) Price range search
        if (minPrice != null && maxPrice != null) {
            return productRepository.findByPriceBetween(minPrice, maxPrice);
        }


        // 5) Recently added
        if ("recent".equals(sort)) {
            return productRepository.findTop10ByOrderByReleaseDateDesc();
        }

        return productRepository.findAll(); // default
    }


    //Delete by id
    public void deleteProduct(Integer id) {
        productRepository.deleteById(id);
    }
}
