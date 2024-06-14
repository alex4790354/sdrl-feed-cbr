package com.github.alex4790354.general.dtoxml.metalResponse;

import lombok.Data;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "Record")
@Data
public class MetalRecord {

    @Attribute(name = "Date")
    private String date;

    @Attribute(name = "Code")
    private String code;

    @Element(name = "Buy")
    private String buy;

    @Element(name = "Sell")
    private String sell;

}
