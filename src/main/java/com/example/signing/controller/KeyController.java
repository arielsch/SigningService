package com.example.signing.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import com.example.signing.service.KeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(path = "/key")
public class KeyController {

	@Autowired
	KeyService keyService;

	@GetMapping
	public ResponseEntity<List<String>> getKeys() {
		List<String> signKeys = keyService.getPublicKeys();
		return new ResponseEntity(signKeys, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<String> createKey(HttpServletResponse response) throws IOException, NoSuchAlgorithmException {
		return new ResponseEntity(keyService.createKey(), HttpStatus.CREATED);
	}

	@DeleteMapping
	public ResponseEntity deleteKey(@RequestParam String key) {
		keyService.deleteKey(key);
		return new ResponseEntity(HttpStatus.OK);
	}


}
