package com.github.alex4790354.general.configuration.feignconfig;

import com.github.alex4790354.general.configuration.exception.CbrException;
import feign.Retryer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import feign.Response;
import feign.codec.Decoder;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.springframework.context.annotation.Bean;
import java.io.*;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;


@Slf4j
@Configuration
@EnableFeignClients(basePackages = "com.github.alex4790354")
public class FeignConfig {

    @Value("${feign.period-seconds}")
    private Integer periodSeconds;

    @Value("${feign.maxPeriod-seconds}")
    private Integer maxPeriodSeconds;

    @Value("${feign.maxAttempts}")
    private Integer maxAttempts;

    @Bean
    public Retryer retryer() {
        return new Retryer.Default(
                TimeUnit.SECONDS.toSeconds(periodSeconds),
                TimeUnit.SECONDS.toSeconds(maxPeriodSeconds),
                maxAttempts);
    }

    @Bean
    public Decoder feignDecoder() {
        return new CombinedDecoder();
    }

    private class CombinedDecoder implements Decoder {
        private final Serializer serializer = new Persister();

        @Override
        public Object decode(Response response, Type type) throws CbrException {

            try {
                if (response.status() != 200 || response.body() == null) {
                    log.error("Failed to process response, status: {}", response.status());
                    throw new CbrException("Failed to process response, status: " + response.status());
                }
                return handleXmlResponse(response, type);
            } catch (Exception e) {
                log.error("Exception in decode. Exception: ", e);
                return new CbrException("Exception in decode. e.getMessage(): {}" + e.getMessage());
            }
        }

        private Object handleXmlResponse(Response response, Type type) throws CbrException {
            Class<?> rawType;
            if (type instanceof ParameterizedType) {
                Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
                if (actualTypeArguments.length > 0 && actualTypeArguments[0] instanceof Class) {
                    rawType = (Class<?>) actualTypeArguments[0];
                } else {
                    log.error("Unsupported generic type for XML deserialization");
                    throw new CbrException("Unsupported generic type for XML deserialization");
                }
            } else if (type instanceof Class) {
                rawType = (Class<?>) type;
            } else {
                log.error("Unsupported type for XML deserialization");
                throw new CbrException("Unsupported type for XML deserialization");
            }

            try (InputStream inputStream = response.body().asInputStream()) {
                InputStreamReader reader = new InputStreamReader(inputStream, Charset.forName("windows-1251"));
                return serializer.read(rawType, reader);
            } catch (Exception e) {
                log.error("Exception in InputStreamReader. Exception: ", e);
                throw new CbrException("Exception in InputStreamReader. e: " + e.getMessage());
            }
        }
    }
}
