package com.example.real.estate.admin.RealEstateAdmin.service;

import com.example.real.estate.admin.RealEstateAdmin.entity.User;
import com.example.real.estate.admin.RealEstateAdmin.mapper.UserMapper;
import com.example.real.estate.admin.RealEstateAdmin.model.request.NotaryUpdateRequest;
import com.example.real.estate.admin.RealEstateAdmin.model.request.StatusRequest;
import com.example.real.estate.admin.RealEstateAdmin.model.request.UsersRequest;
import com.example.real.estate.admin.RealEstateAdmin.model.responce.UserDto;
import com.example.real.estate.admin.RealEstateAdmin.repository.UserRepository;
import com.example.real.estate.admin.RealEstateAdmin.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{
    private final UserValidator userValidator;
    private final UserRepository userRepository;

    @Override
    public Page<UserDto> findUsersBySearch(UsersRequest request) {
        log.info("findUsersBySearch {}", request.search());
        PageRequest pageRequest = PageRequest.of(request.pageNumber(), 5, Sort.by("id").descending());
        Page<User> users = userRepository.findAllByFirstnameOrLastnameStartingWith(request.search(), pageRequest);
        log.info("found {} users", users.getTotalElements());
        return users.map(UserMapper::mapUserToUserDto);
    }

    @Override
    @Transactional
    public void updateStatus(StatusRequest request) {
        log.info("updateStatus for user with id {}",request.userId());
        userRepository.updateStatusById(request.userId(), request.enabled());
        log.info("user status updated");
    }

    @Override
    public Page<UserDto> findNotariesBySearch(UsersRequest request) {
        log.info("findNotariesBySearch {}", request.search());
        PageRequest pageRequest = PageRequest.of(request.pageNumber(), 5, Sort.by("id").descending());
        Page<User> users = userRepository.findAllNotariesByFirstnameOrLastnameStartingWith(request.search(), pageRequest);
        log.info("found {} notaries", users.getTotalElements());
        return users.map(UserMapper::mapUserToUserDto);
    }

    @Override
    public Page<UserDto> findBlacklistBySearch(UsersRequest request) {
        log.info("findBlacklistBySearch {}", request.search());
        PageRequest pageRequest = PageRequest.of(request.pageNumber(), 5, Sort.by("id").descending());
        Page<User> users = userRepository.findAllDisabledByFirstnameOrLastnameStartingWith(request.search(), pageRequest);
        log.info("found {} blacklist", users.getTotalElements());
        return users.map(UserMapper::mapUserToUserDto);
    }

    @Override
    @Transactional
    public void deleteNotary(Long id) {
        log.info("deleteNotary by id {}", id);
        Optional<User> optionalNotary = userRepository.findByIdAndRole(id, "NOTARY");
        if (optionalNotary.isEmpty()){
            log.error("notary with id {} not found", id);
        }
        else {
            userRepository.delete(optionalNotary.get());
            log.info("notary deleted by id {}", id);
        }
    }

    @Override
    public void updateNotary(NotaryUpdateRequest request, BindingResult bindingResult) {
        log.info("updateNotary by id {}", request.userId());

        bindingResult = userValidator.validateNotaryUpdate(request, bindingResult);
        if(bindingResult.hasErrors()){
            log.error("validation errors : {}", bindingResult.getAllErrors());
            return;
        }
        Optional<User> optionalNotary = userRepository.findByIdAndRole(request.userId(), "NOTARY");
        if (optionalNotary.isEmpty()){
            log.error("notary with id {} not found", request.userId());
            bindingResult.addError(new ObjectError("notary", "user not found"));
            return;
        }
        User notary = optionalNotary.get();
        notary.setEmail(request.email());
        notary.setFirstname(request.firstName());
        notary.setLastname(request.lastName());
        notary.setPhone(request.phone());
        userRepository.save(notary);
        log.info("User {} updated", request.userId());
    }
}
