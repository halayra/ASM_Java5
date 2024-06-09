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
@Table(name = "Categories")
public class Categories {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long categoryID;
    @NotBlank(message = "{NotBlank.cate.nameCategory}")
     @Size(min = 5, max = 150, message = "{Size.cate.category}")

    String nameCategory;
}
