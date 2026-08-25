package com.tachibana.projectsekai05.common.utils;

import com.tachibana.projectsekai05.config.JwtProperties;
import com.tachibana.projectsekai05.config.SignProperties;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SecurityUtilTest {

    private static final String DEMO_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC5xkwnlrGnADvYAL3rY9+VGX0bG1FHW5Vp2xtuxZWf5v6fW3Z3fGGGGQj15w1hhg6hWdUh/N2vzRsvF9D8cVB0zvnciJ26FIYmHpWMtS1GpAwdT9MotW3hNAdl2tlbpd5QT+HirFK2aRz4fJYx6sDAopsv4KLrGKWUTgFG292R0BfOims79pfX8frn3tWawDJuAlrtjsYRozCVZW3OJECab5aKR9JlSfPuBgEuIvch8uIofa38VxLYT66VwnYvEk1dSiWM+YS3z7zSJBEXgj244vHXdgCQJ+7MtGSsPeZFMfjlR0JzhV5KhTl52TYu2h9h3sWr9tAL+/bYNX3hzGPrAgMBAAECggEATzxRQfdnuVuDqssSNiJoe+8dqaCap61byoUOK85xVXzQMEbx/d+atvfZlwxf7C8h8Vbf7XmtUizQweLOG8X284NOFe5dG79iQkL8NUIXWBBjLGNeJHhRr+KhnyNbXtgdvLJ9nVEzjCLEHQYCh7LhWKhiOWb+BfkCsoEc0Uy4sdZMnk8rCGyen3tZPASlJeXS8yiA2L23by6Jmsh+8Dbg1BE7gK1MmsgqSnMt186L2lNtG2JiZyIRHKnNzwIY9XEL+vQoFXrIveFJ4aPO8Arb5HgX0kcbKVjcXPnvMhblGiqEWOdBOTHD53qNWdYyvN3Hmp+eqX+crRJHAD5vujWvwQKBgQD4W3Ywn2nq48Km9k+hgokBFekvB1pK9Apk2oAT6nfBqz7izVsKd1dADT9V7s5lnJgqWc8ih7IootgaNV1TmD5HN9bsyKKwbODx33y0U60NXdt8By5SbnjPiFtAmMmhuUWDfFl/TeAUEMM/nzp7SXOz9yOvNaZw2a1E7iY9VaWLqwKBgQC/fdCKa+c/Qh0pBiTb+gn4Uc6TibfoJtxfe+v/gyxqUb0TpUCWPDxyA678utBhGgZL6f8uiM+urVxD01tNyyU+9dtz/QtpljW9jes5sI+QE3vhCWNIvndmigJA8D5wAwJY2aPijAzjh8KZrsYWa39DEzhmQOsCflrD3CcznCJIwQKBgERzePTXyyqZUdoQw0vhnX82GcoDfchw0LiRXIGguxce6rs6HGCbGeomfJFju3FYIrxxelyrB9iZYXR/xtQ2ftvEMTGb7vMvXyRrE4TkFys69jaOCB9iIVwZ2gTI17ICTX169XPX8+2z3QyZB1ZZnVE5yZUN9WzfIhmAhfubsn/3AoGAL7efXs7yqf3EnHNufe55y4o/Kt63mD8OzTBpJ5u9VqUVs66LQKSz/0lRsqKuyHOevA4MS8FF12ZVxoUFjIYhuuWzEN0umdoXfCvAQPjxXVqtlO8s8XSc9k1PsHTtaiYl0JQzVYHGuHAdGwTFUdLpvIIunLRym4V3ArV6g07V8EECgYEAl3Xn2CFFpeiM+yIk6+PJMU0/FCgGSXEC4Ch/R6/GTkoVphJE2t3pPsjT8RfQDUmunqVuQf0q25ZwmPT+VN3cRYrsam/cqfh+SE3Sxt7SmnpU223rY0QM/F5PV+iYji6wYp4rGwsKfEZBN4stm4rnWRFty+pqXGgIdkLfO4HaEpI=";
    private static final String DEMO_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAucZMJ5axpwA72AC962PflRl9GxtRR1uVadsbbsWVn+b+n1t2d3xhhhkI9ecNYYYOoVnVIfzdr80bLxfQ/HFQdM753IiduhSGJh6VjLUtRqQMHU/TKLVt4TQHZdrZW6XeUE/h4qxStmkc+HyWMerAwKKbL+Ci6xillE4BRtvdkdAXzoprO/aX1/H6597VmsAybgJa7Y7GEaMwlWVtziRAmm+WikfSZUnz7gYBLiL3IfLiKH2t/FcS2E+ulcJ2LxJNXUoljPmEt8+80iQRF4I9uOLx13YAkCfuzLRkrD3mRTH45UdCc4VeSoU5edk2LtofYd7Fq/bQC/v22DV94cxj6wIDAQAB";

    @Test
    void passwordEncodeAndMatches() {
        String encoded = PasswordUtil.encode("123456");
        assertTrue(PasswordUtil.matches("123456", encoded));
        assertFalse(PasswordUtil.matches("wrong", encoded));
    }

    @Test
    void rsaSignAndVerifyRoundTrip() {
        SignProperties properties = new SignProperties();
        properties.setPrivateKey(DEMO_PRIVATE_KEY);
        properties.setPublicKey(DEMO_PUBLIC_KEY);
        properties.setAlgorithm("SHA256withRSA");
        SignUtil signUtil = new SignUtil(properties);

        String data = "appId=1001&timestamp=1700000000&nonce=abc123";
        String sign = signUtil.sign(data);
        assertNotNull(sign);
        assertTrue(signUtil.verify(data, sign));
        assertFalse(signUtil.verify(data + "1", sign));
    }

    @Test
    void rsaSignParamsCanonicalize() {
        SignProperties properties = new SignProperties();
        properties.setPrivateKey(DEMO_PRIVATE_KEY);
        properties.setPublicKey(DEMO_PUBLIC_KEY);
        SignUtil signUtil = new SignUtil(properties);

        Map<String, Object> params = new LinkedHashMap<>();
        params.put("name", "bob");
        params.put("age", 18);
        params.put("sign", "ignored");
        String canonical = signUtil.canonicalize(params);
        assertEquals("age=18&name=bob", canonical);

        String sign = signUtil.signParams(params);
        assertTrue(signUtil.verifyParams(params, sign));

        params.put("age", 19);
        assertFalse(signUtil.verifyParams(params, sign));
    }

    @Test
    void jwtGenerateAndParse() {
        JwtProperties properties = new JwtProperties();
        properties.setSecret("ce80f305a644248fc36393ba3d725b85998285020adc269face4052f00592974ca0de6e0b79e70b36c57a769b81449e6");
        properties.setExpiration(7200L);
        JwtUtil jwtUtil = new JwtUtil(properties);

        String token = jwtUtil.generateToken(1L, "admin", "USER");
        assertTrue(jwtUtil.isValid(token));

        Claims claims = jwtUtil.parseToken(token);
        assertEquals("1", claims.getSubject());
        assertEquals("admin", claims.get("username", String.class));
        assertEquals("USER", claims.get("role", String.class));
        assertEquals(1L, jwtUtil.getUserId(token));
        assertEquals("admin", jwtUtil.getUsername(token));
        assertEquals("USER", jwtUtil.getRole(token));
    }

    @Test
    void jwtRejectTamperedToken() {
        JwtProperties properties = new JwtProperties();
        properties.setSecret("ce80f305a644248fc36393ba3d725b85998285020adc269face4052f00592974ca0de6e0b79e70b36c57a769b81449e6");
        JwtUtil jwtUtil = new JwtUtil(properties);

        String token = jwtUtil.generateToken(1L, "admin", "USER");
        String tampered = token.substring(0, token.length() - 2) + "xx";
        assertFalse(jwtUtil.isValid(tampered));
    }
}