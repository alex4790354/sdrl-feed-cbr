package com.github.alex4790354.general.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

 /** Констарны для application.yaml:
  *    schedulers:
  *       currency:...
  *       rates:....
  * */

@Getter
@AllArgsConstructor
public enum DataNameConstant {

    CURRENCY("currency"),

    CURRENCY_RATE("rates"),

    METAL_RATE("metal"),

    RUONIA_RATE("ruonia"),

    RUONIASV_RATE("ruoniasv"),

    KEY_RATE_RATE("keyrate");

    private final String property;

}
