package com.example.signing.service;

import java.io.IOException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface KeyService {
    String createKey() throws IOException, NoSuchAlgorithmException;
    void deleteKey(String publicKey);
    List<String> getPublicKeys();
    KeyPair getKeyPair(String publicKey);
}
