package com.example.real.estate.admin.RealEstateAdmin.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Objects.nonNull;

@Data
@Entity
@Table(name = "users")
@Accessors(chain = true)
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false, unique = true)
    private String email;
    @Column(length = 50, nullable = false, unique = true)
    private String phone;
    @Column(length = 100)
    private String password;
    @Column(length = 25, nullable = false)
    private String firstname;
    @Column(length = 25, nullable = false)
    private String lastname;
    @Lob
    private byte[] avatar;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean enabled;

    @ManyToOne
    private Role role;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
        grantedAuthorities.addAll(role.getPermissions().stream().map(permission -> new SimpleGrantedAuthority(permission.name())).toList());
        return grantedAuthorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return enabled;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            return this.email.equals(((User) obj).email);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.email.hashCode();
    }


}
