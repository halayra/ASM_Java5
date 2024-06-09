package org.phamxuantruong.asm2.Interface;

import org.phamxuantruong.asm2.Entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BillDAO extends JpaRepository<Bill, Long> {
    List<Bill> findByCartId(Long cartId);
}
