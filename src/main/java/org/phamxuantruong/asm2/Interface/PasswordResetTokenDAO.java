package org.phamxuantruong.asm2.Interface;

import org.phamxuantruong.asm2.Entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenDAO extends JpaRepository<PasswordResetToken, Long> {

        PasswordResetToken findByToken(String token);
}
