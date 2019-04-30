import com.github.taoism.apigateway.sdk.exception.SignatureWrongNumberException;
import com.github.taoism.apigateway.sdk.util.Auth;
import com.github.taoism.apigateway.sdk.util.UrlSafeBase64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;

public class Test {
    private final static Logger logger = LoggerFactory.getLogger(Test.class);

    public static void main(String[] args) {
        String accessKey = "ak-test1";
        String secretKey = "sk-test20";
        String url = "http://127.0.0.1:8080/api/test/hello?a=1&b=2";
        HashMap<String, String> body = new HashMap<>(16);
        body.put("c", "1");
        body.put("user", "abc5");
        body.put("b2", "2");

        Auth auth = Auth.create(accessKey, secretKey);
        String s = auth.getSign(url);
        logger.info("--- getSign ---");
        logger.info(s);
        logger.info(new String(UrlSafeBase64.decode(s)));

        String s2 = auth.getSign(url, body);
        System.out.println("");
        logger.info("--- getSign with body ---");
        logger.info(s2);
        logger.info(new String(UrlSafeBase64.decode(s2)));

        System.out.println("");
        logger.info("--- validSign ---");
        try {
            String[] ss = Auth.decodeSign(s2);
            logger.info(Arrays.toString(ss));
            logger.warn("bool:{}", auth.validSign(s2, url, body, Long.valueOf(ss[2])));
        } catch (SignatureWrongNumberException e) {
            logger.warn(e.getMessage());
        }
    }
}
