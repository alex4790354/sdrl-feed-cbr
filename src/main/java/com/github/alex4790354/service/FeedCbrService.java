package com.github.alex4790354.service;

import com.github.alex4790354.general.dtoxml.metalResponse.MetalRateXml;
import com.github.alex4790354.general.dtoxml.valutaResponse.Valuta;
import com.github.alex4790354.general.dtoxml.valcursResponse.ValCurs;


public interface FeedCbrService {

    Valuta getCurrenciesList(int frequencyIndex);

    ValCurs getCurrencyRates(String requestDateAsString);

    void getCurrencyRatesInitialLoad();

    MetalRateXml getMetalRatesDaily();

    MetalRateXml getMetalRates(String pDateStart, String pDateEnd);

    void metalRateInitialLoad();

}
