package com.github.alex4790354.general.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum IdentityConstant {

    JOB_CURRENCY_IDENTITY("currencyLoadJob"),

    JOB_CURRENCY_RATES_IDENTITY("RatesLoadJob"),

    JOB_METAL_RATES_IDENTITY("MetalLoadJob"),

    JOB_RUONIA_IDENTITY("RuoniaLoadJob"),

    JOB_RUONIASV_IDENTITY("RuoniaSVLoadJob"),

    JOB_KEY_RATE_IDENTITY("KeyRateLoadJob");

    private final String identity;

}
