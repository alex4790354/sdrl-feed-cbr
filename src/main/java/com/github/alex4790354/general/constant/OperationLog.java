package com.github.alex4790354.general.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OperationLog {

    JOB_CURRENCIES_LIST("JOB: /currencies-list"),

    JOB_CURRENCY_RATES_LIST("JOB: /currency-rates-list");

    private final String operation;

    public String getJobOperation() {
        return operation;
    }

}
