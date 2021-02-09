package com.example.signing.service.impl;

import com.example.signing.ResourceNotFoundException;
import com.example.signing.service.KeyService;
import com.example.signing.util.SignUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
public class KeyServiceImpl implements KeyService {

    @Autowired
    SignUtil signUtil;

    Map<String, KeyPair> keyMap = new HashMap<>();

    @Override
    public String createKey() throws IOException, NoSuchAlgorithmException {
        KeyPair keyPair = signUtil.createKey();
        String publicKeyId = signUtil.generateKeyId(keyPair.getPublic().getEncoded());
        keyMap.put(publicKeyId, keyPair);
        return publicKeyId;
    }

    @Override
    public void deleteKey(String publicKey) {
        if (keyMap.containsKey(publicKey)){
            keyMap.remove(publicKey);
        }else {
            throw new ResourceNotFoundException();
        }
    }

    @Override
    public List<String> getPublicKeys() {
        return new ArrayList<>(keyMap.keySet());
    }

    @Override
    public KeyPair getKeyPair(String publicKey) {
        if (!keyMap.containsKey(publicKey)){
            //throw
        }
        return keyMap.get(publicKey);
    }
}
