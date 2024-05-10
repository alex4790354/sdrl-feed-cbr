package com.github.alex4790354.general.dtoxml.valcursHistResponse;


import lombok.Data;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Data
@Root(name = "Record")
public class ValRecord {

    @Attribute(name = "Date")
    private String date;

    @Attribute(name = "Id")
    private String id;

    @Element(name = "Nominal")
    private int nominal;

    @Element(name = "Value")
    private String value;

    @Element(name = "VunitRate")
    private String vunitRate;

}

