package org.phamxuantruong.asm2.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Bill")
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long BillID;
    @Column(name = "CartID")
    private Long cartId;
    @Column(name = "ProductID")
    private Long productId;
}
