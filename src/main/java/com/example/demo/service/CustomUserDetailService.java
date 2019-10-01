package com.example.demo.service;

import com.example.demo.entity.UserAccount;
import com.example.demo.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserAccountRepository userAccountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserAccount userAccount = Optional.ofNullable(userAccountRepository.findUserAccountByUsername(username))
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return new User(userAccount.getUsername(), userAccount.getPassword(), Collections.emptyList());
    }
}
