package com.github.alex4790354.general.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** Констарны для работы с датами
 * */

@Getter
@AllArgsConstructor
public enum DateFormatConstant {

   CBR_REQUEST("dd/MM/yyyy"),
   CBR_RESPONSE("dd.MM.yyyy"),
   ISO_DATE_FORMAT("yyyy-MM-dd");

   private final String value;

}
