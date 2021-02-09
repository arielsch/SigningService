package com.example.signing.service.impl;

import com.example.signing.service.KeyService;
import com.example.signing.service.SignService;
import com.example.signing.util.SignUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SignatureException;

@Service
public class SignServiceImpl implements SignService {

    @Autowired
    KeyService keyService;

    @Autowired
    SignUtil signUtil;

    @Override
    public String sign(String data, String publicKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        PrivateKey privateKey = keyService.getKeyPair(publicKey).getPrivate();
        return signUtil.sign(data, privateKey);
    }

    @Override
    public boolean verify(String publicKey, String data, String signature) throws Exception {
        return signUtil.verify(data, signature, keyService.getKeyPair(publicKey).getPublic());
    }
}
