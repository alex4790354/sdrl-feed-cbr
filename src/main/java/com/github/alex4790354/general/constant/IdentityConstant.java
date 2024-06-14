package com.github.alex4790354.general.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum IdentityConstant {

    JOB_CURRENCY_IDENTITY("currencyLoadJob"),

    JOB_CURRENCY_RATES_IDENTITY("RatesLoadJob"),

    JOB_METAL_RATES_IDENTITY("MetalLoadJob");

    private final String identity;

}
