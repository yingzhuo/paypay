package com.github.yingzhuo.paypay.wechatpay.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by admin on 2015/6/30.
 */
public class HttpUtils {

    private static final String DEFAULT_CHARSET = "UTF-8";

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtils.class);

    private static final int TIME_OUT = 20 * 1000;

    /**
     * 发送Get请求
     *
     * @param url
     * @return
     */
    public static String get(String url) {
        LOGGER.info(">> url = {}", url);
//		StringBuilder bufferRes = null;
        try {
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(url, String.class);
            LOGGER.info("<< response = {}", response);
            return response;
        } catch (Exception e) {

            LOGGER.error("<< url = {}", url, e);
            return null;
        }
    }

    public static String get(String url, Map<String, String> params) {
        String res = get(initParams(url, params));
        return res;
    }

    public static String syncFormPost(String url, Map<String, String> params) {
        LOGGER.info(">> url = {}, params = {}", url, params);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        params.forEach(map::add);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        String res = response.getBody();
        LOGGER.info("<< res = {}", res);
        return res;
    }

    public static String syncXmlPost(String url, String xml) {
        LOGGER.info(">> url = {}, xml = {}", url, xml);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType(MediaType.APPLICATION_XML, Charset.forName("utf-8")));
        HttpEntity<String> request = new HttpEntity<>(xml, headers);


        //SpringBoot中，RestTemplate中文乱码解决方案 StringHttpMessageConverter类，默认是的编码是ISO-8859-1：
        SimpleClientHttpRequestFactory httpRequestFactory = new SimpleClientHttpRequestFactory();
        httpRequestFactory.setReadTimeout(35000);
        httpRequestFactory.setConnectTimeout(5000);
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        messageConverters.add(new ByteArrayHttpMessageConverter());

        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        messageConverters.add(stringHttpMessageConverter);
        messageConverters.add(new ResourceHttpMessageConverter());
        messageConverters.add(new SourceHttpMessageConverter());
        messageConverters.add(new AllEncompassingFormHttpMessageConverter());
        RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
        restTemplate.setMessageConverters(messageConverters);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        String res = response.getBody();
        LOGGER.info("<<xml请求的返回结果:{}", res);
        return res;
    }

    public static String initParams(String url, Map<String, String> params) {
        if (null == params || params.isEmpty()) {
            return url;
        }
        StringBuilder sb = new StringBuilder(url);
        if (url.indexOf("?") == -1) {
            sb.append("?");
        } else {
            sb.append("&");
        }
        boolean first = true;
        for (Entry<String, String> entry : params.entrySet()) {
            if (first) {
                first = false;
            } else {
                sb.append("&");
            }
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append("=");
            if (StringUtils.isNotEmpty(value)) {
                try {
                    sb.append(URLEncoder.encode(value, DEFAULT_CHARSET));
                } catch (UnsupportedEncodingException e) {

                    LOGGER.error("url = {}, error = {}", url, e.getMessage(), e);
                }
            }
        }
        return sb.toString();
    }

}
