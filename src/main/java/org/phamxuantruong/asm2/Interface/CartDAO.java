package org.phamxuantruong.asm2.Interface;

import java.util.List;

import org.phamxuantruong.asm2.Entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import jakarta.transaction.Transactional;

public interface CartDAO extends JpaRepository<Cart, Long> {
	
	@Query("SELECT o.cartId FROM Cart o WHERE o.users.username=?1 AND o.product.productID=?2")
	Long findByUsernameAndProductID(String username, Long productId);
	
	@Query("SELECT COUNT(o.cartId) FROM Cart o WHERE o.users.username=?1")
	Long countByUsername(String username);
	
	@Query("SELECT o FROM Cart o WHERE o.users.username=?1")
	List<Cart> findByUsername(String username);	
	
}