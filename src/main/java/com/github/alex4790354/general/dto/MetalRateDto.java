package com.github.alex4790354.general.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.alex4790354.general.constant.DateFormatConstant;
import com.github.alex4790354.general.utils.DateHelper;
import lombok.*;

@Getter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class MetalRateDto {

    @JsonProperty("date")
    private String date; // String in format dd.mm.yyyy. Example: "23.11.2023"

    @Setter
    @JsonProperty("code")
    private String code;

    @JsonProperty("buy")
    private String buy;

    @JsonProperty("sell")
    private String sell;

    public void setDate(String date) {

        this.date = DateHelper.getNewDateAsString(date,
                DateFormatConstant.CBR_RESPONSE.getValue(),
                DateFormatConstant.ISO_DATE_FORMAT.getValue());
    }

    public void setBuy(String buy) {
        this.buy = buy.replace(",", ".");
    }

    public void setSell(String sell) {
        this.sell = sell.replace(",", ".");
    }

}
