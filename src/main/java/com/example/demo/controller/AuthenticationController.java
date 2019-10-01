package com.example.demo.controller;

import com.example.demo.dto.UserAccountDto;
import com.example.demo.entity.UserAccount;
import com.example.demo.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserAccountRepository userAccountRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/signUp")
    public ResponseEntity<UserAccountDto> signUp(@RequestBody UserAccountDto userAccountDto) {
        ModelMapper modelMapper = new ModelMapper();
        userAccountDto.setPassword(bCryptPasswordEncoder.encode(userAccountDto.getPassword()));
        UserAccount userAccount = modelMapper.map(userAccountDto, UserAccount.class);
        userAccountRepository.save(userAccount);
        userAccountDto = modelMapper.map(userAccount, UserAccountDto.class);
        return new ResponseEntity<>(userAccountDto, HttpStatus.CREATED);
    }
}
