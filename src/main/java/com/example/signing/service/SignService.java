package com.example.signing.service;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

public interface SignService {
    String sign(String data, String publicKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException;
    boolean verify(String publicKey, String data, String signature) throws Exception;
}
