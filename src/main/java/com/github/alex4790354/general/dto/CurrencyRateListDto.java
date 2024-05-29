package com.github.alex4790354.general.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.alex4790354.general.configuration.exception.CbrException;
import com.github.alex4790354.general.constant.DateFormatConstant;
import com.github.alex4790354.general.dtoxml.valcursResponse.Valute;
import com.github.alex4790354.general.utils.DateHelper;
import com.github.alex4790354.general.dtoxml.valcursResponse.ValCurs;
import com.github.alex4790354.general.dtoxml.valcursHistResponse.ValCursHist;
import com.github.alex4790354.general.dtoxml.valcursHistResponse.ValRecord;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
public class CurrencyRateListDto {


    @JsonProperty("Foreign CurrencyDto Market Lib")
    protected List<CurrencyRateDto> currencyRateList;

    public void setRatesFromDailyRateList(ValCurs valCurs) {
        List<CurrencyRateDto> currencyCurrencyRateDtoList = new ArrayList<>();
        CurrencyRateDto currencyRateDto;
        for (Valute item : valCurs.getValutes()) {
            try {
                currencyRateDto = new CurrencyRateDto(item.getId(),
                        item.getNumCode(),
                        item.getCharCode(),
                        item.getNominal(),
                        item.getName(),
                        new BigDecimal(item.getValue().replace(",", ".")),
                        DateHelper.getNewDateAsString(valCurs.getDate(),
                                DateFormatConstant.CBR_RESPONSE.getValue(),
                                DateFormatConstant.ISO_DATE_FORMAT.getValue())
                );
                currencyCurrencyRateDtoList.add(currencyRateDto);
            } catch (Exception e) {
                log.error("Exception in setRatesFromDailyRateList with item: {}. Exception: ", item.toString(), e);
                throw new CbrException("Exception in setRatesFromDailyRateList with item: " + item.toString() + ". Exception: " + e.getMessage());
            }
        }
        currencyRateList = currencyCurrencyRateDtoList;
    }

    public void setRatesFromHistoricalRateList(ValCursHist valCurs, CurrencyDto currencyDto) {
        List<CurrencyRateDto> currencyCurrencyRateDtoList = new ArrayList<>();
        CurrencyRateDto currencyRateDto;
        for (ValRecord valRecord : valCurs.getValRecords()) {
            try {
                currencyRateDto = new CurrencyRateDto(
                        valRecord.getId(),
                        0,
                        currencyDto.getCharCode(),
                        valRecord.getNominal(),
                        currencyDto.getNameEng(),
                        new BigDecimal(valRecord.getValue().replace(",", ".")),
                        DateHelper.getNewDateAsString(valRecord.getDate(),
                                DateFormatConstant.CBR_RESPONSE.getValue(),
                                DateFormatConstant.ISO_DATE_FORMAT.getValue())
                );
                currencyCurrencyRateDtoList.add(currencyRateDto);
            } catch (Exception e) {
                log.error("Exception in setRatesFromDailyRateList with item: {}. Exception: ", valRecord.toString(), e);
                throw new CbrException("\"Exception in setRatesFromDailyRateList with item: " + valRecord.toString() + ". Exception: " +  e.getMessage());
            }
        }
        currencyRateList = currencyCurrencyRateDtoList;
    }

}
