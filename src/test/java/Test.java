import com.dataqin.apigateway.sdk.exception.SignatureWrongNumberException;
import com.dataqin.apigateway.sdk.util.Auth;
import com.dataqin.apigateway.sdk.util.UrlSafeBase64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;

public class Test {
    private final static Logger logger = LoggerFactory.getLogger(Test.class);

    public static void main(String[] args) {
        String accessKey = "dataqin-ak-test1";
        String secretKey = "dataqin-sk-test20";
        String url = "http://127.0.0.1:8889/mortgage/api/v1/cancel/queryInfo";

        long L1 = System.currentTimeMillis();
        Auth auth = Auth.create(accessKey, secretKey);
        long L2 = System.currentTimeMillis();
        String signature = auth.getSign(url);
        long L3 = System.currentTimeMillis();

        logger.info("L2-L1={}, L3-L2={}", L2-L1, L3-L2);
        logger.info(signature);
        logger.info(new String(UrlSafeBase64.decode(signature)));

        System.out.println("");
        logger.info("--- validSign ---");
        try {
            String[] ss = Auth.decodeSign(signature);
            logger.info(Arrays.toString(ss));
            logger.warn("bool:{}", auth.validSign(signature, url, Long.valueOf(ss[2])));
        } catch (SignatureWrongNumberException e) {
            logger.warn(e.getMessage());
        }
    }
}
