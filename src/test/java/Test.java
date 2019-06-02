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

        Auth auth = Auth.create(accessKey, secretKey);
        String s = auth.getSign(url);
        logger.info("--- getSign ---");
        logger.info(s);
        logger.info(new String(UrlSafeBase64.decode(s)));

        HashMap<String, Object> body = new HashMap<>(16);

        body.put("cxrmc", "ddc");
        body.put("zjh", "35220319874561890207");
        body.put("yhjgdm", "10001");
        body.put("djjgid", "610000");
        body.put("bdcdjzmh", "wx2019");
        body.put("lcdm", 5301);
        body.put("timestamp", 1258711455);

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
