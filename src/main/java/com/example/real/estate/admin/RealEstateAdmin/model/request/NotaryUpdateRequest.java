package com.example.real.estate.admin.RealEstateAdmin.model.request;

public record NotaryUpdateRequest(
        Long userId,
        String firstName,
        String lastName,
        String phone,
        String email
) {
}
