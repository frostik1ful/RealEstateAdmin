package com.example.real.estate.admin.RealEstateAdmin.service;

import com.example.real.estate.admin.RealEstateAdmin.model.request.NotaryUpdateRequest;
import com.example.real.estate.admin.RealEstateAdmin.model.request.StatusRequest;
import com.example.real.estate.admin.RealEstateAdmin.model.request.UsersRequest;
import com.example.real.estate.admin.RealEstateAdmin.model.responce.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;

public interface UserService {
    Page<UserDto> findUsersBySearch(UsersRequest request);

    void updateStatus(StatusRequest request);

    Page<UserDto> findNotariesBySearch(UsersRequest request);

    Page<UserDto> findBlacklistBySearch(UsersRequest request);

    void deleteNotary(Long id);

    void updateNotary(NotaryUpdateRequest request, BindingResult bindingResult);
}
