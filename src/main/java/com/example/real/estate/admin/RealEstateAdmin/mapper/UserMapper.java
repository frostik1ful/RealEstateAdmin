package com.example.real.estate.admin.RealEstateAdmin.mapper;

import com.example.real.estate.admin.RealEstateAdmin.entity.User;
import com.example.real.estate.admin.RealEstateAdmin.model.responce.UserDto;

public interface UserMapper {
     static UserDto mapUserToUserDto(User user){
        return new UserDto(user.getId(), user.getFirstname(), user.getLastname(), user.getEmail(), user.getRole().getName(), user.getPhone(), user.isEnabled());
    }
}
