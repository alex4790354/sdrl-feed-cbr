package com.github.alex4790354.controller;

import com.github.alex4790354.general.constant.StatusConstant;
import com.github.alex4790354.general.dtoxml.metalResponse.MetalRateXml;
import com.github.alex4790354.general.dtoxml.valcursResponse.ValCurs;
import com.github.alex4790354.general.dtoxml.valutaResponse.Valuta;
import com.github.alex4790354.service.FeedCbrService;
import feign.Headers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 *  Контроллер ручной выгрузки данных из ЦБ РФ
 * *
 *  Страница загрузки списка валют:
 *  <a href="https://www.cbr.ru/scripts/XML_val.asp?d=0">CBR</a>
 *
 * @author Alexander Vasilev
 * */

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/scripts")
public class FeedCbrManualController {

    private final FeedCbrService service;


    /**                         XML_val.asp - currencies List
     * *
     * request example: <a href="http://server_name:server_port/scripts/XML_val.asp?d=0">Today Daily currencies list (xml)</a>
     * will call:       <a href="https://www.cbr.ru/scripts/XML_val.asp?d=0">Daily CBR currencies list (xml)</a>
    */
    @GetMapping(value = "/XML_val.asp", params = {"d"})
    @Headers("Content-Type: application/xml")
    public ResponseEntity<Valuta> getCbrRequest(@RequestParam(value = "d") int frequencyIndex) {
        return ResponseEntity.ok(service.getCurrenciesList(frequencyIndex));
    }


    /**                         XML_daily.asp - currencies rate by date
     * *
     * request example: <a href="http://server_name:server_port/scripts/XML_val.asp?d=0"> Daily currencies Rate (xml) for 15/11/2023</a>
     * will call:       <a href="https://www.cbr.ru/scripts/XML_daily.asp?date_req=15/11/2023">CBR Daily exchange rates example</a>
     */
    @GetMapping(value = "/XML_daily.asp", params = {"date_req"})
    @Headers("Content-Type: application/xml")
    public ResponseEntity<ValCurs> getCurrenciesRatesOnDate(@RequestParam(value = "date_req") String requestDateAsString) {
        return ResponseEntity.ok(service.getCurrencyRates(requestDateAsString));
    }


    /**                         currency-rate/initial-load  -- initial load
     * *
     * request example: <a href="http://server_name:server_port/scripts/currency-rate/initial-load> Historical initial currencies rate load.</a>
     * will call:
     * <a href="https://www.cbr.ru/scripts/XML_dynamic.asp?date_req1=12/01/2010&date_req2=28/11/2023&VAL_NM_RQ=R01235"> CBR Historical exchange rates from 12/01/2010 up to Today date</a>
     */
    @GetMapping(value = "/currency-rate/initial-load")
    @Headers("Content-Type: application/xml")
    public ResponseEntity<String> getHistoricalCurrencyRates() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(service::getCurrencyRatesInitialLoad);
        return ResponseEntity.accepted().body(StatusConstant.ACCEPTED_FOR_PROCESSING.getStatus());
    }


    /**                         metalRate (for Today date)
     * *
     *  Request example: <a href="http://localhost:8084/scripts/metalRate">This call</a> will call
     **       ==>>       <a href="http://www.cbr.ru/scripts/xml_metall.asp?date_req1=28/11/2023&date_req2=28/11/2023">in CBR</a>*/
    @GetMapping(value = "/metalRate")
    @Headers("Content-Type: application/xml")
    public ResponseEntity<MetalRateXml> getMetalsRate() {
        return ResponseEntity.ok(service.getMetalRatesDaily());
    }


    /**                         metalRate (for given Date)
     * *
     *   Request example: <a href="http://localhost:8084/scripts/metalByDate?date_req=01/11/2023">This call</a> will call
     **       ==>>        <a href="http://www.cbr.ru/scripts/xml_metall.asp?date_req1=01/11/2023&date_req2=01/11/2023">in CBR</a>*/
    @GetMapping(value = "/metalByDate", params = {"date_req"})
    @Headers("Content-Type: application/xml")
    public ResponseEntity<MetalRateXml> getMetalsRateByDate(@RequestParam(value = "date_req") String requestDateAsString) {
        return ResponseEntity.ok(service.getMetalRates(requestDateAsString, requestDateAsString));
    }


    /**                         metalRates Initial Load
     * *
     *   Request example: <a href="http://localhost:8084/scripts/metalByDate?date_req=01/11/2023">This call</a> will call
     **       ==>>        <a href="http://www.cbr.ru/scripts/xml_metall.asp?date_req1=01/11/2023&date_req2=01/11/2023">in CBR</a>*/
    @GetMapping(value = "/metal-rate/initial-load")
    @Headers("Content-Type: application/xml")
    public ResponseEntity<String> getMetalRatesInitialLoad() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(service::metalRateInitialLoad);
        return ResponseEntity.accepted().body(StatusConstant.ACCEPTED_FOR_PROCESSING.getStatus());
    }
}
