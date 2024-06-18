package com.example.real.estate.admin.RealEstateAdmin.validator;

import com.example.real.estate.admin.RealEstateAdmin.model.request.NotaryUpdateRequest;
import com.example.real.estate.admin.RealEstateAdmin.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

@Component
@AllArgsConstructor
public class UserValidatorImpl implements UserValidator {
    private final UserRepository userRepository;
    public BindingResult validateNotaryUpdate(NotaryUpdateRequest request, BindingResult bindingResult) {
        if (request.firstName().isEmpty() || request.firstName().isBlank()) {
            bindingResult.addError(new FieldError("request","firstName", "first name required"));
        }
        if (request.firstName().length() > 25 || request.firstName().length() < 3){
            bindingResult.addError(new FieldError("request","firstName", "The number of characters must be between 3 and 25"));
        }
        if (request.lastName().isEmpty() || request.lastName().isBlank()) {
            bindingResult.addError(new FieldError("request","lastName", "last name required"));
        }
        if (request.lastName().length() > 25 || request.lastName().length() < 3){
            bindingResult.addError(new FieldError("request","lastName", "The number of characters must be between 3 and 25"));
        }

        if (request.phone().isEmpty() || request.phone().isBlank()) {
            bindingResult.addError(new FieldError("request","phone", "phone required"));
        }

        if (request.email().isEmpty() || request.email().isBlank()) {
            bindingResult.addError(new FieldError("request","email", "email required"));
        }

        if (bindingResult.hasErrors()){
            return bindingResult;
        }

        if (userRepository.existsByPhoneAndIdNot(request.phone(), request.userId())){
            bindingResult.addError(new FieldError("request","phone", "user with that phone number already exists"));
        }

        if (userRepository.existsByEmailAndIdNot(request.phone(), request.userId())){
            bindingResult.addError(new FieldError("request","phone", "user with that email already exists"));
        }

        return bindingResult;
    }
}
