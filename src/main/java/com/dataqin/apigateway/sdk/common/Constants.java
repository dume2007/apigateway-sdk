package com.dataqin.apigateway.sdk.common;

import java.nio.charset.Charset;
/**
 * SDK相关配置常量
 * @author Administrator
 */
public final class Constants {
    /**
     * 版本号
     */
    public static final String VERSION = "1.0.0";
    /**
     * 所有都是UTF-8编码
     */
    public static final Charset UTF_8 = Charset.forName("UTF-8");
    /**
     * 连接超时时间 单位秒(默认10s)
     */
    public static final int CONNECT_TIMEOUT = 10;

    private Constants() {
    }
}
