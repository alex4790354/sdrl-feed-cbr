package com.github.alex4790354.general.configuration.feignconfig;


import com.github.alex4790354.general.configuration.exception.CbrException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import feign.Response;
import feign.codec.Decoder;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import java.io.*;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Slf4j
@Configuration
public class FeignConfig {

    @Value("${feign.period-seconds}")
    private Integer periodSeconds;

    @Value("${feign.maxPeriod-seconds}")
    private Integer maxPeriodSeconds;

    @Value("${feign.maxAttempts}")
    private Integer maxAttempts;


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
                boolean isSoapResponse = isSoapResponse(response);
                if (isSoapResponse) {
                    try {
                        return handleSoapResponse(response, type);
                    } catch (Exception e) {
                        log.error("Error in handleSoapResponse. Response: {}, type: {}", response.toString(), type.toString());
                        throw new CbrException("Error in handleSoapResponse.\nException: " + e.getMessage());
                    }
                } else {
                    return handleXmlResponse(response, type);
                }
            } catch (Exception e) {
                log.error("Exception in decode. Exception: ", e);
                return new CbrException("Exception in decode. e.getMessage(): {}" + e.getMessage());
            }
        }

        private boolean isSoapResponse(Response response) {
            // Проверка наличия SOAP-специфичных заголовков
            String contentType = response.headers().get("Content-Type").stream()
                    .findFirst()
                    .orElse("");
            return contentType.contains("application/soap+xml");
        }

        private Object handleSoapResponse(Response response, Type type) throws Exception {
            String rawXml = new BufferedReader(new InputStreamReader(response.body().asInputStream(), StandardCharsets.UTF_8))
                    .lines().collect(Collectors.joining("\n"));

            InputStream streamForDeserialization = new ByteArrayInputStream(rawXml.getBytes(StandardCharsets.UTF_8));

            if (type instanceof Class) {
                return serializer.read((Class<?>) type, streamForDeserialization);
            } else if (type instanceof ParameterizedType parameterizedType) {
                Type rawType = parameterizedType.getRawType();

                if (rawType.equals(ResponseEntity.class)) {
                    Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                    if (actualTypeArguments.length == 1 && actualTypeArguments[0] instanceof Class) {
                        Object deserializedObject = serializer.read((Class<?>) actualTypeArguments[0], streamForDeserialization);
                        return ResponseEntity.ok(deserializedObject);
                    }
                }
                log.error("Cannot handle parameterized type: {}", type.toString());
                throw new CbrException("Cannot handle parameterized type: " + type.toString());
            } else {
                log.error("Cannot handle type: {}", type.toString());
                throw new CbrException("Cannot handle type: " + type.toString());
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
                String a = "Hello world";
                return serializer.read(rawType, reader);
            } catch (Exception e) {
                log.error("Exception in InputStreamReader. Exception: ", e);
                throw new CbrException("Exception in InputStreamReader. e: " + e.getMessage());
            }
        }
    }
}
