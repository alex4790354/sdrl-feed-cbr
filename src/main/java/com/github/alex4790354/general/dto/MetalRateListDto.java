package com.github.alex4790354.general.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MetalRateListDto {


    @JsonProperty("from_date")
    private String fromDate; // Format yyyymmdd. Example: "20231128"

    @JsonProperty("to_date")
    private String toDate; // Format yyyymmdd. Example: "20231128"

    @JsonProperty("name")
    private String name;

    @JsonProperty("Record")
    private List<MetalRateDto> metalRecords;


}
