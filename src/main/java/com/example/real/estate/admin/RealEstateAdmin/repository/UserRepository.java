package com.example.real.estate.admin.RealEstateAdmin.repository;

import com.example.real.estate.admin.RealEstateAdmin.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findFirstByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.role.name IN ('ADMIN', 'CLIENT', 'BUILDER') AND (:search IS NULL OR (LOWER(u.firstname) LIKE LOWER(CONCAT( '%', :search, '%')) OR LOWER(u.lastname) LIKE LOWER(CONCAT( '%', :search, '%'))))")
    Page<User> findAllByFirstnameOrLastnameStartingWith(String search,  Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.role.name = 'NOTARY' AND (:search IS NULL OR (LOWER(u.firstname) LIKE LOWER(CONCAT( '%', :search, '%')) OR LOWER(u.lastname) LIKE LOWER(CONCAT( '%', :search, '%'))))")
    Page<User> findAllNotariesByFirstnameOrLastnameStartingWith(String search,  Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.enabled = FALSE AND (:search IS NULL OR (LOWER(u.firstname) LIKE LOWER(CONCAT( '%', :search, '%')) OR LOWER(u.lastname) LIKE LOWER(CONCAT( '%', :search, '%'))))")
    Page<User> findAllDisabledByFirstnameOrLastnameStartingWith(String search,  Pageable pageable);

    @Modifying
    @Query("UPDATE User u SET u.enabled = :enabled WHERE u.id = :id")
    void updateStatusById(Long id, Boolean enabled);

    @Query("SELECT u FROM User u WHERE u.id = :id AND u.role.name = :role")
    Optional<User> findByIdAndRole(Long id, String role);

    boolean existsByPhoneAndIdNot(String phone, Long id);

    boolean existsByEmailAndIdNot(String email, Long id);
}
