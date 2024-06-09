package org.phamxuantruong.asm2.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long productID;

    @NotNull(message = "{NotNull.product.categoryID}")
    @PositiveOrZero(message = "{PositiveOrZero.product.categoryID}")
    Long categoryID;

    @NotBlank(message = "{NotBlank.product.name}")
    @Size(min = 5, max = 150, message = "{Size.product.name}")
    String name;

    @NotBlank(message = "{NotBlank.product.author}")
    @Size(min = 5, max = 150, message = "{Size.product.author}")
    String author;

    @NotBlank(message = "{NotBlank.product.year}")
    @Pattern(regexp = "^\\d{4}$", message = "{Pattern.product.year}")
    String year;

    @NotNull(message = "{NotNull.product.price}")
    Double price;

    @NotNull(message = "{NotNull.product.quantity}")
    Integer quantity;
//
//    @NotBlank(message = "{NotBlank.product.image}")
    String image;

    @NotBlank(message = "{NotBlank.product.infor}")
    String infor;
}

