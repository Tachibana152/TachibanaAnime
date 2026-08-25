package com.tachibana.projectsekai05.common.utils;

import com.tachibana.projectsekai05.config.SignProperties;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * RSA 签名 / 验签工具（SHA256withRSA）
 * <p>用于开放接口的防篡改与身份校验：发送方用私钥签名，接收方用公钥验签。</p>
 */
@Component
public class SignUtil {

    private final SignProperties properties;

    public SignUtil(SignProperties properties) {
        this.properties = properties;
    }

    /**
     * 私钥签名，返回 base64 签名
     */
    public String sign(String data) {
        try {
            Signature signature = Signature.getInstance(properties.getAlgorithm());
            signature.initSign(loadPrivateKey());
            signature.update(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(signature.sign());
        } catch (Exception e) {
            throw new IllegalStateException("RSA 签名失败", e);
        }
    }

    /**
     * 公钥验签
     */
    public boolean verify(String data, String signBase64) {
        try {
            Signature signature = Signature.getInstance(properties.getAlgorithm());
            signature.initVerify(loadPublicKey());
            signature.update(data.getBytes(StandardCharsets.UTF_8));
            return signature.verify(Base64.getDecoder().decode(signBase64));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 对参数集合按字典序拼接后签名（常用签名规则）
     */
    public String signParams(java.util.Map<String, Object> params) {
        String canonical = canonicalize(params);
        return sign(canonical);
    }

    /**
     * 对参数集合按字典序拼接后验签
     */
    public boolean verifyParams(java.util.Map<String, Object> params, String signBase64) {
        String canonical = canonicalize(params);
        return verify(canonical, signBase64);
    }

    /**
     * 参数按 key 字典序拼接为 key1=value1&key2=value2
     */
    public String canonicalize(java.util.Map<String, Object> params) {
        return params.entrySet().stream()
                .filter(e -> e.getKey() != null && e.getValue() != null)
                .filter(e -> !"sign".equals(e.getKey()))
                .sorted(java.util.Map.Entry.comparingByKey())
                .map(e -> e.getKey() + "=" + e.getValue())
                .reduce((a, b) -> a + "&" + b)
                .orElse("");
    }

    private PrivateKey loadPrivateKey() {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(sanitize(properties.getPrivateKey()));
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            return KeyFactory.getInstance("RSA").generatePrivate(spec);
        } catch (Exception e) {
            throw new IllegalStateException("私钥解析失败", e);
        }
    }

    private PublicKey loadPublicKey() {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(sanitize(properties.getPublicKey()));
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            return KeyFactory.getInstance("RSA").generatePublic(spec);
        } catch (Exception e) {
            throw new IllegalStateException("公钥解析失败", e);
        }
    }

    /**
     * 去除空白与 PEM 头尾标记，兼容裸 base64 与 PEM 格式
     */
    private String sanitize(String key) {
        if (key == null) {
            throw new IllegalArgumentException("密钥不能为空，请配置 sign.private-key / sign.public-key");
        }
        return key.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");
    }
}
