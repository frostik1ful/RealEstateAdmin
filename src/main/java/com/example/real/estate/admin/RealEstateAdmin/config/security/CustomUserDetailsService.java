package com.example.real.estate.admin.RealEstateAdmin.config.security;

import com.example.real.estate.admin.RealEstateAdmin.entity.User;
import com.example.real.estate.admin.RealEstateAdmin.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findFirstByEmail(username);
        if (optionalUser.isEmpty()){
            throw new UsernameNotFoundException("User not found");
        }
        return optionalUser.get();
    }
}
