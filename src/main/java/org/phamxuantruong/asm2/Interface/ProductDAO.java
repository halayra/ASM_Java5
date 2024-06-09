package org.phamxuantruong.asm2.Interface;

import org.phamxuantruong.asm2.Entity.Categories;
import org.phamxuantruong.asm2.Entity.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductDAO extends JpaRepository<Product, Long> {


    void deleteById(Long id);

    Page<Product> findByNameContaining(String name, Pageable pageable);
    Page<Product> findByCategoryIDAndNameContaining(Long categoryID, String name, Pageable pageable);
}
