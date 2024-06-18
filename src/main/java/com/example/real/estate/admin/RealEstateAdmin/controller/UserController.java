package com.example.real.estate.admin.RealEstateAdmin.controller;

import com.example.real.estate.admin.RealEstateAdmin.mapper.ErrorMapper;
import com.example.real.estate.admin.RealEstateAdmin.model.request.NotaryUpdateRequest;
import com.example.real.estate.admin.RealEstateAdmin.model.request.StatusRequest;
import com.example.real.estate.admin.RealEstateAdmin.model.request.UsersRequest;
import com.example.real.estate.admin.RealEstateAdmin.model.responce.UserDto;
import com.example.real.estate.admin.RealEstateAdmin.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping()
    public String users(){
        return "pages/all/users";
    }

    @GetMapping("/notaries")
    public String notaries(){
        return "pages/all/notaries";
    }

    @GetMapping("/black-list")
    public String blackList(){
        return "pages/all/blackList";
    }

    @PostMapping("/get-all")
    public ResponseEntity<Page<UserDto>> getAllUsers(@RequestBody UsersRequest request){
        return ResponseEntity.ok(userService.findUsersBySearch(request));
    }

    @PostMapping("/get-all-notaries")
    public ResponseEntity<Page<UserDto>> getAllNotaries(@RequestBody UsersRequest request){
        return ResponseEntity.ok(userService.findNotariesBySearch(request));
    }

    @PostMapping("/get-all-blacklist")
    public ResponseEntity<Page<UserDto>> getAllBlacklist(@RequestBody UsersRequest request){
        return ResponseEntity.ok(userService.findBlacklistBySearch(request));
    }

    @PostMapping("/update-status")
    public ResponseEntity<?> updateStatus(@RequestBody StatusRequest request){
        userService.updateStatus(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/notary/{id}")
    public ResponseEntity<?> deleteNotary(@PathVariable Long id){
        userService.deleteNotary(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/notary")
    public ResponseEntity<?> updateNotary(@RequestBody NotaryUpdateRequest request, BindingResult bindingResult){
        userService.updateNotary(request, bindingResult);
        if (bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(ErrorMapper.mapErrors(bindingResult));
        }
        return ResponseEntity.ok().build();
    }
}
