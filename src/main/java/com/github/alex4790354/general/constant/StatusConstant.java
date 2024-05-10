package com.github.alex4790354.general.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusConstant {

    SUCCESS("SUCCESS"),
    ACCEPTED_FOR_PROCESSING("Процесс первоначальной загрузки данных запущен... Пожалуйста, проверьте обновление данных через 2-4 минуты.");

    private final String status;

}
