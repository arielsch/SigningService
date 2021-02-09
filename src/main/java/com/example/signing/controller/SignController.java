package com.example.signing.controller;

import com.example.signing.service.SignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

@RestController
@RequestMapping(path = "/sign")
public class SignController {

    @Autowired
    SignService signService;

    @PostMapping
    public ResponseEntity<String> sign(@RequestParam String key, @RequestParam String data) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        return new ResponseEntity(signService.sign(data, key), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Boolean> verify(@RequestParam String key, @RequestParam String data, @RequestBody String signature) throws Exception {
        return new ResponseEntity(signService.verify(key, data, signature), HttpStatus.OK);
    }
}
