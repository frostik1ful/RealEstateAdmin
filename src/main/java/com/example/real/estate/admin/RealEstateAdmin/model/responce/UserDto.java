package com.example.real.estate.admin.RealEstateAdmin.model.responce;

public record UserDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        String role,
        String phone,
        boolean isEnabled
) {
}
