package com.example.assignment_three_zelora.controller;

import com.example.assignment_three_zelora.model.entitys.Customer;
import com.example.assignment_three_zelora.model.entitys.Product;
import com.example.assignment_three_zelora.model.entitys.Wishlist;

import com.example.assignment_three_zelora.model.repos.CustomerRepository;
import com.example.assignment_three_zelora.model.repos.WishlistRepository;
import com.example.assignment_three_zelora.model.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class WishlistController {

    @Autowired
    private ProductService productService;

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private CustomerRepository customerRepository; // 需要有这个 repo

    // 为了简化，先固定使用 customer_id = 1 的用户
    private Customer getCurrentCustomer() {
        Optional<Customer> opt = customerRepository.findById(1);
        return opt.orElse(null);
    }

    // 1) Add to wishlist
    @GetMapping("/wishlist/add")
    public String addToWishlist(@RequestParam Integer productId) {

        Product product = productService.getProductById(productId);
        Customer customer = getCurrentCustomer();

        if (product == null || customer == null) {
            return "redirect:/products/" + productId;
        }

        Wishlist wishlist = new Wishlist();
        // 如果表是自增主键，可以不 setWishlistId
        wishlist.setProductId(product);
        wishlist.setCustomerId(customer);
        wishlist.setWishlistName("My Wishlist");
        wishlist.setNotes("Added from product page");
        wishlist.setAddedDate(new Date());

        wishlistRepository.save(wishlist);

        return "redirect:/products/" + productId;
    }

    // 2) 显示当前用户的 wishlist 列表
    @GetMapping("/wishlist")
    public String viewWishlist(Model model) {
        Customer customer = getCurrentCustomer();
        if (customer == null) {
            model.addAttribute("wishlists", List.of());
        } else {
            List<Wishlist> list = wishlistRepository.findAll()
                    .stream()
                    .filter(w -> w.getCustomerId() != null &&
                            w.getCustomerId().getCustomerId().equals(customer.getCustomerId()))
                    .toList();
            model.addAttribute("wishlists", list);
        }
        return "wishlist"; // templates/wishlist.html
    }

    // 3) 删除 wishlist 项
    @GetMapping("/wishlist/delete/{id}")
    public String deleteWishlist(@PathVariable Integer id) {
        wishlistRepository.deleteById(id);
        return "redirect:/wishlist";
    }
}
