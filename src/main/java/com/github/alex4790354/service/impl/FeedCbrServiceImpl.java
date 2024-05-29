package com.github.alex4790354.service.impl;

import com.github.alex4790354.general.configuration.AppConfig;
import com.github.alex4790354.general.configuration.exception.CbrException;
import com.github.alex4790354.general.dto.CurrenciesListDto;
import com.github.alex4790354.general.dto.CurrencyRateListDto;
import com.github.alex4790354.general.dto.CurrencyDto;
import com.github.alex4790354.general.dto.MetalRateListDto;
import com.github.alex4790354.controller.feing.FeingClientCbr;
import com.github.alex4790354.general.configuration.rabbitmqconfig.RabbitConfig;
import com.github.alex4790354.general.constant.DateFormatConstant;
import com.github.alex4790354.general.constant.RequestFrequency;
import com.github.alex4790354.general.dtoxml.metalResponse.MetalRateXml;
import com.github.alex4790354.general.dtoxml.valcursHistResponse.ValCursHist;
import com.github.alex4790354.general.utils.DateHelper;
import com.github.alex4790354.repository.FeederCbrRepository;
import com.github.alex4790354.service.FeedCbrService;
import feign.FeignException;
import com.github.alex4790354.general.dtoxml.valutaResponse.Valuta;
import com.github.alex4790354.general.dtoxml.valcursResponse.ValCurs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedCbrServiceImpl implements FeedCbrService {

    private final FeingClientCbr feingClientCbr;
    private final FeederCbrRepository feederCbrRepository;
    private final RabbitConfig rabbitConfig;
    private final RabbitTemplate rabbitTemplate;
    private final AppConfig appConfig;

    @Override
    public Valuta getCurrenciesList(int frequencyIndex) {

        Valuta valutaResult = new Valuta();

        try {

            valutaResult = feingClientCbr.feinGetCbr(RequestFrequency.DAILY.getValue());
            CurrenciesListDto currenciesListDto = new CurrenciesListDto();
            currenciesListDto.setCurrenciesFromValuta(valutaResult, String.valueOf(RequestFrequency.DAILY));
            rabbitTemplate.convertAndSend(rabbitConfig.getCbrCurrenciesExchange(),
                    StringUtils.EMPTY,
                    currenciesListDto.getCurrencies());
            log.info("List of currencies with size={} records successfully loaded into MQ-queue", currenciesListDto.getCurrencies().size());
            return valutaResult;
        }
        catch (FeignException e) {
            log.error("Error in getCurrenciesList. \n Message --> ", e);
            throw new CbrException("Error in getCurrenciesList. Status:" + e.status() + " . Message: " + e.getMessage());
        }
    }

    @Override
    public ValCurs getCurrencyRates(String requestDateAsString) {
        ValCurs valCursDailyResult = new ValCurs();
        String vRequestDateAsString;
        try {
            if (requestDateAsString.isEmpty()) {
                vRequestDateAsString = DateHelper.getTodateDateMinusDaysAsString(DateFormatConstant.CBR_REQUEST.getValue(), 0);
            } else {
                vRequestDateAsString = requestDateAsString;
            }
            valCursDailyResult = feingClientCbr.feinGetRatesOnDate(vRequestDateAsString);
            log.info("Load current RATEsOnDate list  size: {}", valCursDailyResult.getValutes().size());

            CurrencyRateListDto currencyRateListDto = new CurrencyRateListDto();
            currencyRateListDto.setRatesFromDailyRateList(valCursDailyResult);

            rabbitTemplate.convertAndSend(rabbitConfig.getCbrCurrencyRateExchange(),
                    StringUtils.EMPTY,
                    currencyRateListDto.getCurrencyRateList());
            log.info("List of currency Rates, with size={} records, successfully loaded into MQ-queue", currencyRateListDto.getCurrencyRateList().size());

            return valCursDailyResult;
        } catch (FeignException e) {
            log.error("Error in getCurrencyRates. Message --> ", e);
            throw new CbrException("Error in getCurrencyRates. Status: " + e.status() + ", Message --> " + e.getMessage());
        }
    }


    @Override
    public void getCurrencyRatesInitialLoad() {

        ValCursHist valCursHistoricalResult = new ValCursHist();
        try {
            List<CurrencyDto> currencyDtos = feederCbrRepository.getAllCurrencies();
            String vDateEnd = DateHelper.getTodateDateMinusDaysAsString(DateFormatConstant.CBR_REQUEST.getValue(), 0);
            if (currencyDtos.isEmpty()) {
                log.info("We just got an empty currency list.");
            } else {
                for (CurrencyDto currencyDto : currencyDtos) {
                    try {
                        log.info("Going to proceed request for CharCode={}, ID={}.", currencyDto.getCharCode(), currencyDto.getId());
                        valCursHistoricalResult =
                                feingClientCbr.feinGetHistoricalRates(appConfig.getHistCurRatesStartDate(),
                                        vDateEnd,
                                        currencyDto.getId());
                        CurrencyRateListDto currencyRateListDto = new CurrencyRateListDto();
                        currencyRateListDto.setRatesFromHistoricalRateList(valCursHistoricalResult, currencyDto);

                        rabbitTemplate.convertAndSend(rabbitConfig.getCbrCurrencyRateExchange(),
                                StringUtils.EMPTY,
                                currencyRateListDto.getCurrencyRateList());
                        log.info("List of currency Rates, with size={} records, successfully loaded into MQ-queue", currencyRateListDto.getCurrencyRateList().size());
                    } catch (Exception e) {
                        log.error("Exception in Loop getCurrencyRatesInitialLoad() for request with histCurRatesStartDate = {}, vDateEnd = {}, currencyDto = {}, Exception: "
                                ,appConfig.getHistCurRatesStartDate()
                                ,vDateEnd
                                ,currencyDto.toString()
                                ,e);
                    }
                }
            }
        } catch (Exception e) {
            log.error("getCurrencyRatesInitialLoad() Error", e);
            throw new CbrException("Exception in getCurrencyRatesInitialLoad. Message --> " + e.getMessage());
        }
    }

    @Override
    public MetalRateXml getMetalRatesDaily() {

        String vDateReqStart = DateHelper.getTodateDateMinusDaysAsString(DateFormatConstant.CBR_REQUEST.getValue(), appConfig.getMetalStartDayFromToday());    // in format "dd/MM/yyyy". Example: 25/11/2023
        String vDateReqEnd = DateHelper.getTodateDateMinusDaysAsString(DateFormatConstant.CBR_REQUEST.getValue(), 0);
        return getMetalRates(vDateReqStart, vDateReqEnd);

    }

    @Override
    public MetalRateXml getMetalRates(String pDateStart, String pDateEnd) {

        MetalRateXml metalRateXmlResult = new MetalRateXml();
        try {

            log.info("going to get metal rates. vDateReqStart: {}, vDateReqEnd: {}", pDateStart, pDateEnd);
            metalRateXmlResult = feingClientCbr.feinGetMetalsRates(pDateStart, pDateEnd);

            if (null == metalRateXmlResult.getMetalRecords()) {

                log.info("getMetalsOnDate() finished with status = . getRecords().size()= null." ); //, status);

            } else {

                log.info("Load current metalRateXmlResult list  with status  XXX and size: {}", metalRateXmlResult.getMetalRecords().size());
                ModelMapper modelMapper = new ModelMapper();
                MetalRateListDto metalRateListDto = modelMapper.map(metalRateXmlResult, MetalRateListDto.class);
                rabbitTemplate.convertAndSend(rabbitConfig.getCbrMetalExchange(),
                    StringUtils.EMPTY,
                    metalRateListDto.getMetalRecords());
                log.info("List of Metal Rates, with size={} records, successfully loaded into MQ-queue", metalRateListDto.getMetalRecords().size());

            }
            return metalRateXmlResult;

        } catch (FeignException e) {
            log.error("Error getMetalsOnDate", e);
            throw new CbrException("Error getMetalsOnDate. (DateStart: " + pDateStart + ", DateEnd: " + pDateEnd + ").\n Message --> " + e.getMessage());
        }
    }


    public void metalRateInitialLoad() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateFormatConstant.CBR_REQUEST.getValue());
        LocalDate startDate = LocalDate.parse(appConfig.getHistMetalRatesStartDate(), formatter);
        LocalDate currentDate = LocalDate.now();
        LocalDate nextStartDate = startDate;

        while (nextStartDate.getYear() <= currentDate.getYear()) {

            LocalDate endDate;

            if (nextStartDate.getYear() == currentDate.getYear()) {
                endDate = currentDate;
            } else {
                endDate = nextStartDate.withDayOfYear(nextStartDate.lengthOfYear());
            }

            getMetalRates(formatter.format(nextStartDate), formatter.format(endDate));
            // Move to next year
            nextStartDate = nextStartDate.plusYears(1).withDayOfYear(1);
        }
    }
}
