package org.phamxuantruong.asm2.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Users")
public class Users {
    @Id
    @NotBlank(message = "{NotBlank.user.username}")
    @Size(max = 50, message = "{Size.user.username}")
    private String username;

    @NotBlank(message = "{NotBlank.user.password}")
    private String password;

    @Transient
    private String repeatPassword;

    @NotBlank(message = "{NotBlank.user.fullname}")
    private String fullname;

    @NotBlank(message = "{NotBlank.user.email}")
    @Email(message = "{Email.user.email}")
    private String email;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date ngaysinh;

    private String gender;

    @Column(name = "phone_number")
    @NotBlank(message = "{NotBlank.user.phoneNumber}")
    private String phoneNumber;

    private String image;
    private Boolean admin;
}
