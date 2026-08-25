package com.tachibana.projectsekai05.common.utils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * RSA 密钥对生成工具
 * <p>用于生成 base64 编码的私钥/公钥，配置到 application.yml 的 sign.* 属性中</p>
 */
public final class RsaKeyGenerator {

    private RsaKeyGenerator() {
    }

    public static final int KEY_SIZE = 2048;

    /**
     * 生成 RSA 密钥对，打印 base64 私钥/公钥（运行 main 即可生成）
     */
    public static void main(String[] args) throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(KEY_SIZE);
        KeyPair keyPair = generator.generateKeyPair();
        String privateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
        String publicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
        System.out.println("private-key: " + privateKey);
        System.out.println("public-key:  " + publicKey);
    }
}
