package com.github.taoism.apigateway.sdk.util;

import com.github.taoism.apigateway.sdk.exception.SignatureExpireException;
import com.github.taoism.apigateway.sdk.exception.SignatureWrongNumberException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Administrator
 */
public class Auth {
    private final static Logger logger = LoggerFactory.getLogger(Auth.class);

    private final String accessKey;
    private final SecretKeySpec secretKey;
    /**
     * 默认10分钟有效
     */
    private static final long EXPIRE = 600;
    /**
     * 签名分割数组长度必须为3
     */
    private static final int SIGNATURE_ARRAY_LEN = 3;

    private Auth(String accessKey, SecretKeySpec secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public static Auth create(String accessKey, String secretKey) {
        if (StringUtils.isNullOrEmpty(accessKey) || StringUtils.isNullOrEmpty(secretKey)) {
            throw new IllegalArgumentException("empty key");
        }
        byte[] sk = StringUtils.utf8Bytes(secretKey);
        SecretKeySpec secretKeySpec = new SecretKeySpec(sk, "HmacSHA1");
        return new Auth(accessKey, secretKeySpec);
    }

    private Mac createMac() {
        Mac mac;
        try {
            mac = javax.crypto.Mac.getInstance(secretKey.getAlgorithm());
            mac.init(secretKey);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }
        return mac;
    }

    /**
     * 将post参数数组根据key排序后生成以&连接的字符串
     * @param data
     * @return
     */
    private String toQueryString(Map<?, ?> data)
    {
        StringBuilder queryString = new StringBuilder();
        Object[] key = data.keySet().toArray();
        Arrays.sort(key);

        for (Object o : key) {
            try {
                queryString.append(o).append("=");
                queryString.append(URLEncoder.encode((String) data.get(o), "UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                // do nothing
            }
        }

        if (queryString.length() > 0) {
            queryString.deleteCharAt(queryString.length() - 1);
        }

        return queryString.toString();
    }

    /**
     * 解析签名
     * @param encodeSignature
     * @return String[]
     */
    public static String[] decodeSign(String encodeSignature) {
        String decodeOriginSignature = new String(UrlSafeBase64.decode(encodeSignature));
        String[] arrayOriginSignature = decodeOriginSignature.split(":");
        if (arrayOriginSignature.length != SIGNATURE_ARRAY_LEN) {
            throw new SignatureWrongNumberException("Signature decode error");
        }
        return arrayOriginSignature;
    }

    /**
     * 验证签名
     * @param sign
     * @param urlString
     * @param deadline
     * @param body
     * @return
     */
    public boolean validSign(String sign, String urlString, HashMap<String, String> body, long deadline) {
        long currentTime = System.currentTimeMillis() / 1000;
        if (currentTime > deadline) {
            throw new SignatureExpireException("Signature expire");
        }

        String currentSign = getSign(urlString, body, deadline);
        logger.info("headSign:{}, currentSign:{}, url:{}, body:{}, deadline:{}",sign, currentSign, urlString, body, deadline);
        return sign.equals(currentSign);
    }

    /**
     * get请求生成签名
     * @param urlString urlString
     * @return String
     */
    public String getSign(String urlString) {
        long deadline = System.currentTimeMillis() / 1000 + EXPIRE;
        return getSign(urlString, null, deadline);
    }

    /**
     * post请求生成签名
     * @param urlString
     * @param body
     * @return
     */
    public String getSign(String urlString, HashMap<String, String> body) {
        long deadline = System.currentTimeMillis() / 1000 + EXPIRE;
        return getSign(urlString, body, deadline);
    }

    /**
     * 包含过期值的签名
     * @param urlString
     * @param body
     * @param deadline
     * @return
     */
    private String getSign(String urlString, HashMap<String, String> body, long deadline) {
        URI uri = URI.create(urlString);
        String path = uri.getRawPath();
        String query = uri.getRawQuery();

        Mac mac = createMac();
        mac.update(StringUtils.utf8Bytes(path));

        if (query != null && query.length() != 0) {
            mac.update((byte) ('?'));
            mac.update(StringUtils.utf8Bytes(query));
        }
        mac.update((byte) deadline);
        mac.update((byte) '\n');
        if (null != body && body.size() > 0) {
            mac.update(StringUtils.utf8Bytes(toQueryString(body)));
        }

        String digest = UrlSafeBase64.encodeToString(mac.doFinal());
        return UrlSafeBase64.encodeToString(this.accessKey + ":" + digest + ":" + deadline);
    }
}
