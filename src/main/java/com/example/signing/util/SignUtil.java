package com.example.signing.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.io.*;
import java.security.*;
import java.util.Base64;
import static java.nio.charset.StandardCharsets.UTF_8;

@Component
public class SignUtil {

    @Value("${key.generator.algorithm:RSA}")
    String keyGeneratorAlgorithm;

    @Value("${sign.generator.algorithm:SHA256withRSA}")
    String signAlgorithm;

    @Value("${sign.generator.key.size:2048}")
    int keySize;

    KeyPairGenerator kpg;
    KeyFactory kf;

    @PostConstruct
    public void postConstruct() throws NoSuchAlgorithmException {
        kpg = KeyPairGenerator.getInstance(keyGeneratorAlgorithm);
        kpg.initialize(Integer.valueOf(keySize));
        kf = KeyFactory.getInstance(keyGeneratorAlgorithm);
    }

    public KeyPair createKey(){
        return kpg.generateKeyPair();
    }

    public String sign(String data, PrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature privateSignature = Signature.getInstance(signAlgorithm);
        privateSignature.initSign(privateKey);
        privateSignature.update(data.getBytes(UTF_8));
        byte[] signature = privateSignature.sign();
        return Base64.getEncoder().encodeToString(signature);
    }

    public boolean verify(String data, String signature, PublicKey publicKey) throws Exception {
        Signature publicSignature = Signature.getInstance(signAlgorithm);
        publicSignature.initVerify(publicKey);
        publicSignature.update(data.getBytes(UTF_8));
        byte[] signatureBytes = Base64.getDecoder().decode(signature);
        return publicSignature.verify(signatureBytes);
    }

    public String generateKeyId(byte[] publicKey) throws IOException, NoSuchAlgorithmException {
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        try (InputStream input = new ByteArrayInputStream(publicKey)) {
            byte[] buffer = new byte[8192];
            int len = input.read(buffer);
            while (len != -1) {
                sha1.update(buffer, 0, len);
                len = input.read(buffer);
            }
            return new HexBinaryAdapter().marshal(sha1.digest());
        }
    }
}
