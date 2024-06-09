package org.phamxuantruong.asm2.Interface;

import org.phamxuantruong.asm2.Entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsersDAO extends JpaRepository<Users, String> {
    List<Users> findByAdminIsNull(Pageable pageable);
    List<Users> findByAdminIsNotNull();
    Page<Users> findByUsernameContainingAndFullnameContainingAndAdminIsNull(
            String username, String fullname, Pageable pageable);
    Page<Users> findByUsernameContainingAndFullnameContainingAndAdminIsNotNull(
            String username, String fullname, Pageable pageable);

    Optional<Users> findByUsername(String username);


    List<Users> findAllByUsername(String username);

    Users findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<Users> findByPhoneNumber(String phoneNumber);
}

