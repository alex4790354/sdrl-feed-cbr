package com.github.alex4790354.general.configuration.rabbitmqconfig;


import lombok.Getter;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@EnableConfigurationProperties
public class RabbitConfig {

    @Value("${spring.rabbitmq.cbr.exchange}")
    private String cbrCurrenciesExchange;

    @Value("${spring.rabbitmq.rates.exchange}")
    private String cbrCurrencyRateExchange;

    @Value("${spring.rabbitmq.metal.exchange}")
    private String cbrMetalExchange;

    @Bean
    public FanoutExchange spxFeedCbrCurrenciesExchange() {
        return new FanoutExchange(cbrCurrenciesExchange);
    }

    @Bean
    public FanoutExchange spxFeedCbrCurrencyRateExchange() {
        return new FanoutExchange(cbrCurrencyRateExchange);
    }

    @Bean
    public FanoutExchange spxFeedCbrMetalExchange() {
        return new FanoutExchange(cbrMetalExchange);
    }
}