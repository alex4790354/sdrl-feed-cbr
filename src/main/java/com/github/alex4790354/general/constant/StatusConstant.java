package com.github.alex4790354.general.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusConstant {

    SUCCESS("SUCCESS"),
    ACCEPTED_FOR_PROCESSING("The initial data download process has started... Please check the data update in 1-2 minutes.");

    private final String status;

}
