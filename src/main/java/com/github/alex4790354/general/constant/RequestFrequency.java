package com.github.alex4790354.general.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RequestFrequency {

    /** Параметры запросов валют. Коды валют
     *    d=0 - устанавливаемые ежедневно.
     *    d=1 - устанавливаемые ежемесячно.     *
     *    <a href="https://www.cbr.ru/development/SXML/">CBR</a>
     * */

    DAILY(0),
    MONTHLY(1);

    private final int value;

}
