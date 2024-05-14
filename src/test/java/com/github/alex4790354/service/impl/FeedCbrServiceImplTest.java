package com.github.alex4790354.service.impl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.alex4790354.controller.feing.FeingClientCbr;
import com.github.alex4790354.general.configuration.rabbitmqconfig.RabbitConfig;
import com.github.alex4790354.general.dto.CurrenciesListDto;
import com.github.alex4790354.general.dtoxml.valutaResponse.Valuta;
import com.github.alex4790354.repository.FeederCbrRepository;
import com.google.gson.JsonElement;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;


@ExtendWith(MockitoExtension.class)
class FeedCbrServiceImplTest {

    @Mock
    private FeingClientCbr feingClientCbr;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private RabbitConfig rabbitConfig;

    @Mock
    private FeederCbrRepository feederCbrRepository;

    private FeedCbrServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new FeedCbrServiceImpl(feingClientCbr, feederCbrRepository, rabbitConfig, rabbitTemplate);

    }


    @Test
    public void testGetCurrenciesList_ReturnsCorrectJson() throws Exception {
        Gson gson = new Gson();

        String expected = new String(Files.readAllBytes(Paths.get("src/test/resources/sdrl-feed-example/currency-list.json")));
        JsonElement expectedJson = gson.fromJson(expected, JsonElement.class);

        String xmlContent = new String(Files.readAllBytes(Paths.get("src/test/resources/cbr-response-example/currency-list.xml")));
        Valuta valutaFromXmlContent = createValutaFromXml(xmlContent);

        when(feingClientCbr.feinGetCbr(anyInt())).thenReturn(valutaFromXmlContent);
        ArgumentCaptor<String> exchangeCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> routingKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Object> messageCaptor = ArgumentCaptor.forClass(Object.class);
        Valuta valutaFromService = service.getCurrenciesList(0);

        verify(rabbitTemplate).convertAndSend(exchangeCaptor.capture(), routingKeyCaptor.capture(), messageCaptor.capture());
        String current = getJsonAsString(valutaFromService);
        JsonElement currentJson = gson.fromJson(current, JsonElement.class);

        assertEquals(expectedJson, currentJson, "JSON objects are not equal");
    }


    private Valuta createValutaFromXml(String xmlContent) {
        Serializer serializer = new Persister();
        try {
            StringReader reader = new StringReader(xmlContent);
            return serializer.read(Valuta.class, reader);
        } catch (Exception e) {
            throw new RuntimeException("Failed to unmarshal Valuta from XML", e);
        }
    }

    private String getJsonAsString(Valuta valuta) {
        String jsonResult = "";
        ObjectMapper mapper = new ObjectMapper();
        try {
            CurrenciesListDto currenciesListDto = new CurrenciesListDto();
            currenciesListDto.setCurrenciesFromValuta(valuta, "DAILY");
            jsonResult = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(currenciesListDto.getCurrencies());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonResult;
    }
}