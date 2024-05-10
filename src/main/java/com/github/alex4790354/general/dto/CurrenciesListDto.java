package com.github.alex4790354.general.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import com.github.alex4790354.general.dtoxml.valutaResponse.Valuta;
import com.github.alex4790354.general.dtoxml.valutaResponse.Item;
import java.util.ArrayList;
import java.util.List;

@Data
public class CurrenciesListDto {

    @JsonProperty("Foreign CurrencyDto Market Lib")
    protected List<CurrencyDto> currencies;

    public void setCurrenciesFromValuta(Valuta valuta, String frequency) {

        List<CurrencyDto> newCurrenciesList = new ArrayList<>();
        CurrencyDto currencyDto;
        for (Item item : valuta.getItems()) {
            currencyDto = new CurrencyDto(item.getId(),
                    item.getName(),
                    item.getEngName(),
                    item.getNominal(),
                    item.getParentCode().trim(),
                    frequency);
            newCurrenciesList.add(currencyDto);
        }
        currencies = newCurrenciesList;
    }

}
