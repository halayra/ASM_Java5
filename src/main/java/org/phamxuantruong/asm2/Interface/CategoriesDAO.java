package org.phamxuantruong.asm2.Interface;

import org.phamxuantruong.asm2.Entity.Categories;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriesDAO extends JpaRepository<Categories, Long> {
    Page<Categories> findByNameCategoryContaining(String nameCategory, Pageable pageable);
    // Lọc theo categoryID
    Page<Categories> findByCategoryID(Long categoryID, Pageable pageable);
}
