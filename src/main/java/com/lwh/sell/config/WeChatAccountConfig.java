package com.lwh.sell.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author lwh
 * @date 2020-02-01
 * @desp
 */
@Data
@Component
@ConfigurationProperties(prefix = "wechat")
public class WeChatAccountConfig {

    private String mpAppId;

    private String mpAppSecret;

    private String openAppId;

    private String openAppSecret;

    /**
     * 商户号
     */
    private String mchId;

    /**
     * 商户秘钥
     */
    private String mchKey;

    /**
     * 商户证书路径
     */
    private String keyPath;

    /**
     * 支付异步回调URL
     */
    private String notifyUrl;

    /**
     * 微信模板消息
     */
    private Map<String, String> templateId;
}
