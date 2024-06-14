package com.github.alex4790354.general.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrencyDto {

    @JsonProperty("id")
    protected String id;

    @JsonProperty("name_rus")
    protected String nameRus;

    @JsonProperty("name_eng")
    protected String nameEng;

    @JsonProperty("nominal")
    protected long nominal;

    @JsonProperty("parent_code")
    protected String parentCode;

    @JsonProperty("frequency")
    protected String frequency;

    @JsonProperty("char_code")
    protected String charCode;

    public CurrencyDto(String id, String nameRus, String nameEng, long nominal, String parentCode, String frequency) {
        this.id = id;
        this.nameRus = nameRus;
        this.nameEng = nameEng;
        this.nominal = nominal;
        this.parentCode = parentCode;
        this.frequency = frequency;
        this.charCode = "xxx?xxx";
    }
}
