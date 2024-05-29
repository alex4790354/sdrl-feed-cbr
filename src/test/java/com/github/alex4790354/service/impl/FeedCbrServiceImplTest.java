package com.github.alex4790354.service.impl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.alex4790354.controller.feing.FeingClientCbr;
import com.github.alex4790354.general.configuration.rabbitmqconfig.RabbitConfig;
import com.github.alex4790354.general.dto.CurrenciesListDto;
import com.github.alex4790354.general.dto.CurrencyRateListDto;
import com.github.alex4790354.general.dto.MetalRateListDto;
import com.github.alex4790354.general.dtoxml.metalResponse.MetalRateXml;
import com.github.alex4790354.general.dtoxml.valcursResponse.ValCurs;
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
import org.modelmapper.ModelMapper;
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
        Valuta valutaFromXmlContent = (Valuta) createObjectFromXml(xmlContent, Valuta.class);

        when(feingClientCbr.feinGetCbr(anyInt())).thenReturn(valutaFromXmlContent);
        ArgumentCaptor<String> exchangeCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> routingKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Object> messageCaptor = ArgumentCaptor.forClass(Object.class);
        Valuta valutaFromService = service.getCurrenciesList(0);

        verify(rabbitTemplate).convertAndSend(exchangeCaptor.capture(), routingKeyCaptor.capture(), messageCaptor.capture());
        String current = getValutaJsonAsString(valutaFromService);
        JsonElement currentJson = gson.fromJson(current, JsonElement.class);

        assertEquals(expectedJson, currentJson, "JSON-1 objects are not equal");
    }


    @Test
    void getCurrencyRates_ShouldReturnValidResponse() throws Exception {
        Gson gson = new Gson();

        String expected = new String(Files.readAllBytes(Paths.get("src/test/resources/sdrl-feed-example/currency-rate.json")));
        JsonElement expectedJson = gson.fromJson(expected, JsonElement.class);

        String xmlContent = new String(Files.readAllBytes(Paths.get("src/test/resources/cbr-response-example/currency-rate.xml")));
        ValCurs expectedValCurs = (ValCurs) createObjectFromXml(xmlContent, ValCurs.class);
        when(feingClientCbr.feinGetRatesOnDate(anyString())).thenReturn(expectedValCurs);

        String requestDate = "14/05/2024";
        ArgumentCaptor<String> exchangeCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> routingKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Object> messageCaptor = ArgumentCaptor.forClass(Object.class);

        ValCurs valCursFromService = service.getCurrencyRates(requestDate);
        String current = getValCursJsonAsString(valCursFromService);
        JsonElement currentJson = gson.fromJson(current, JsonElement.class);

        verify(feingClientCbr).feinGetRatesOnDate(eq(requestDate));
        verify(rabbitTemplate).convertAndSend(exchangeCaptor.capture(), routingKeyCaptor.capture(), messageCaptor.capture());
        assertNotNull(current, "String from getValCursJsonAsString(valCursFromService) is NULL");

        assertEquals(expectedJson, currentJson, "JSON-2 objects are not equal");
    }


    @Test
    void getMetalRates_ShouldReturnValidResponse() throws Exception {
        Gson gson = new Gson();

        String expected = new String(Files.readAllBytes(Paths.get("src/test/resources/sdrl-feed-example/metal-rate.json")));
        JsonElement expectedJson = gson.fromJson(expected, JsonElement.class);

        String xmlContent = new String(Files.readAllBytes(Paths.get("src/test/resources/cbr-response-example/metal-rate.xml")));
        MetalRateXml expectedMetalRateXml = (MetalRateXml) createObjectFromXml(xmlContent, MetalRateXml.class);
        when(feingClientCbr.feinGetMetalsRates(anyString(), anyString())).thenReturn(expectedMetalRateXml);

        String requestDate = "14/05/2024";
        ArgumentCaptor<String> exchangeCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> routingKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Object> messageCaptor = ArgumentCaptor.forClass(Object.class);

        MetalRateXml metalRateXmlFromService = service.getMetalRates(requestDate, requestDate);
        ModelMapper modelMapper = new ModelMapper();
        MetalRateListDto metalRateListDtoFromService = modelMapper.map(metalRateXmlFromService, MetalRateListDto.class);

        String current = getMetalRateListDtoJsonAsString(metalRateListDtoFromService);
        JsonElement currentJson = gson.fromJson(current, JsonElement.class);

        // Verify the correct method and capture the correct method parameters
        verify(feingClientCbr).feinGetMetalsRates(eq(requestDate), eq(requestDate));
        verify(rabbitTemplate).convertAndSend(exchangeCaptor.capture(), routingKeyCaptor.capture(), messageCaptor.capture());
        assertNotNull(current, "String from getValCursJsonAsString(valCursFromService) is NULL");

        assertEquals(expectedJson, currentJson, "JSON-3 objects are not equal");
    }


    private Object createObjectFromXml(String xmlContent, Class clazz) {
        Serializer serializer = new Persister();
        try {
            StringReader reader = new StringReader(xmlContent);
            return serializer.read(clazz, reader);
        } catch (Exception e) {
            throw new RuntimeException("Failed to unmarshal Valuta from XML", e);
        }
    }

    private String getValutaJsonAsString(Valuta valuta) {
        String jsonAsStringResult = "";
        ObjectMapper mapper = new ObjectMapper();
        try {
            CurrenciesListDto currenciesListDto = new CurrenciesListDto();
            currenciesListDto.setCurrenciesFromValuta(valuta, "DAILY");
            jsonAsStringResult = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(currenciesListDto.getCurrencies());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonAsStringResult;
    }

    private String getMetalRateListDtoJsonAsString(MetalRateListDto metalRateListDto) {
        String jsonAsStringResult = "";
        ObjectMapper mapper = new ObjectMapper();
        try {
            jsonAsStringResult = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(metalRateListDto.getMetalRecords());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonAsStringResult;
    }

    private String getValCursJsonAsString(ValCurs valCurs) {
        String jsonAsStringResult = "";
        ObjectMapper mapper = new ObjectMapper();
        try {
            CurrencyRateListDto currencyRateListDto = new CurrencyRateListDto();
            currencyRateListDto.setRatesFromDailyRateList(valCurs);
            jsonAsStringResult = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(currencyRateListDto.getCurrencyRateList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonAsStringResult;
    }
}