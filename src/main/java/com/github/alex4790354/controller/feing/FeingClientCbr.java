package com.github.alex4790354.controller.feing;

import com.github.alex4790354.general.configuration.feignconfig.FeignConfig;
import com.github.alex4790354.general.dtoxml.metalResponse.MetalRateXml;
import com.github.alex4790354.general.dtoxml.valcursHistResponse.ValCursHist;
import com.github.alex4790354.general.dtoxml.valcursResponse.ValCurs;
import com.github.alex4790354.general.dtoxml.valutaResponse.Valuta;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * Интерфейс фейн клиента удаленного вызова для выгрузки данных из СРБИ.
 * <a href="https://www.cbr.ru/currency_base/daily/">Official exchange rates for a given date, set daily</a>
 * <a href="https://www.cbr.ru/development/SXML/">Doc: Retrieving data using XML</a>
 * *
 * <a href="https://www.cbr.ru/scripts/XML_val.asp?d=0">Daily currencies list (xml)</a>
 * <a href="https://www.cbr.ru/scripts/XML_daily.asp?date_req=20/11/2023">Daily exchange rates example</a>
 * <a href="https://www.cbr.ru/scripts/XML_dynamic.asp?date_req1=01/01/2023&date_req2=14/11/2023&VAL_NM_RQ=R01235">Historical exchange rates example</a>
 *
 * @author Alexander Vasilev
 */
@FeignClient(name = "FeedCbr", url = "${feign.url-client}", configuration = FeignConfig.class)
public interface FeingClientCbr {

    /**                         Daily currencies list upload
     * *
     *  - Once a day
     *  - daily in 15:00
     *  - from: <a href="https://www.cbr.ru/scripts/XML_val.asp?d=0">Daily currencies list (xml)</a>
     *  - with repeat until success
     * <p>
     * <a href="https://www.cbr.ru/development/SXML/">Reference</a>
     *
     * @return Return generated.daily.Valuta with currencies list (Items)
     */
    //
    @GetMapping(value="/scripts/XML_val.asp", params = {"d"})
    @Headers("Content-Type: application/xml")
    Valuta feinGetCbr(@RequestParam(value = "d") int frequencyIndex);


    /**                          Daily currencies RATE list upload:
     * *
     *  - Once a day
     *  - daily in 15:30
     *  - from: <a href="https://www.cbr.ru/scripts/XML_daily.asp?date_req=02/03/2002">Daily exchange rates example</a>
     *  - with repeat until success
     *  - dateAsString in dd/MM/yyyy format
     * <a href="https://www.cbr.ru/development/SXML/">Reference</a>
     *
     * @return Return generated.Valuta with currencies rate list (Items)
     */
    @GetMapping(value="/scripts/XML_daily.asp", params = {"date_req"})
    @Headers("Content-Type: application/xml")
    ValCurs feinGetRatesOnDate(@RequestParam(value = "date_req") String dateAsString);


    /**                         Initial exchange rates loading (in big dates range)
     * *
     *  - Once a day
     *  - Manual loading
     *  - Example: <a href="https://www.cbr.ru/scripts/XML_dynamic.asp?date_req1=01/01/2023&date_req2=21/11/2023&VAL_NM_RQ=R01235">Historical exchange rates example</a>
     *  - dateAsString in dd/MM/yyyy format.
     *
     * @return Return generated.Valuta with currencies rate list (Items) in big given dates range.
     */
    @GetMapping(value="/scripts/XML_dynamic.asp")
    @Headers("Content-Type: application/xml")
    ValCursHist feinGetHistoricalRates(@RequestParam(value = "date_req1") String dateStart,
                                                                    @RequestParam(value = "date_req2") String dateEnd,
                                                                    @RequestParam(value = "VAL_NM_RQ") String valutaId);


    /**                             Metals rates loading in given dates range
     * *
     *  - Request example: <a href="http://www.cbr.ru/scripts/xml_metall.asp?date_req1=01/11/2023&date_req2=01/11/2023">Metal rates example</a>
     *  - dateAsString in dd/MM/yyyy format.
     *  *
     * @return Returns metal rates list
     */
    @GetMapping(value="/scripts/xml_metall.asp")
    @Headers("Content-Type: application/xml")
    MetalRateXml feinGetMetalsRates(@RequestParam(value = "date_req1") String dateStart,
                                    @RequestParam(value = "date_req2") String dateEnd);

}

