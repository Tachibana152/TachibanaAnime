package com.tachibana.projectsekai05.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * RSA 签名配置
 */
@Data
@ConfigurationProperties(prefix = "sign")
public class SignProperties {

    /** base64 编码的私钥 */
    private String privateKey;

    /** base64 编码的公钥 */
    private String publicKey;

    /** 签名算法 */
    private String algorithm = "SHA256withRSA";

    /** 是否启用签名校验 */
    private boolean enabled = false;
}
