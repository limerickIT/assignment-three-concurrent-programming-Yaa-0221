package com.example.assignment_three_zelora.model.repos;

import com.example.assignment_three_zelora.model.entitys.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Integer> {

}
