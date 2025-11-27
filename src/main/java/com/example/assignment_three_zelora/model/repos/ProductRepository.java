package com.example.assignment_three_zelora.model.repos;


import com.example.assignment_three_zelora.model.entitys.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends
        JpaRepository<Product, Integer>,

        JpaSpecificationExecutor<Product> {

    // 1) 按名称（忽略大小写）
    List<Product> findByProductNameContainingIgnoreCase(String name);

    // 2) 按分类名称
    List<Product> findByCategoryId_CategoryNameContainingIgnoreCase(String categoryName);

    // 3) 按价格范围
    List<Product> findByPriceBetween(BigDecimal min, BigDecimal max);


    // 5) 最近发布的 TOP 10
    List<Product> findTop10ByOrderByReleaseDateDesc();

}