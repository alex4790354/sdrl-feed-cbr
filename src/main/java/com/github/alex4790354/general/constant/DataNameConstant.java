package com.github.alex4790354.general.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

 /** Constants:
  *    schedulers:
  *       currency:...
  *       rates:....
  * */

@Getter
@AllArgsConstructor
public enum DataNameConstant {

    CURRENCY("currency"),

    CURRENCY_RATE("rates"),

    METAL_RATE("metal");

    private final String property;

}
