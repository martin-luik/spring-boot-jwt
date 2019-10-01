package com.example.demo.controller;

import com.example.demo.entity.StringResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<StringResponse> home() {
        return new ResponseEntity<>(new StringResponse("Back-end is up"), HttpStatus.OK);
    }
}
