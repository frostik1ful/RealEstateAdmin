package com.example.real.estate.admin.RealEstateAdmin.validator;

import com.example.real.estate.admin.RealEstateAdmin.model.request.NotaryUpdateRequest;
import org.springframework.validation.BindingResult;

public interface UserValidator {
    public BindingResult validateNotaryUpdate(NotaryUpdateRequest request, BindingResult bindingResult);
}
