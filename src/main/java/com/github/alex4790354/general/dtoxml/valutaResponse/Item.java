package com.github.alex4790354.general.dtoxml.valutaResponse;


import lombok.Data;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "Item")
@Data
public class Item {
    @Attribute(name = "ID")
    private String id;

    @Element(name = "Name")
    private String name;

    @Element(name = "EngName")
    private String engName;

    @Element(name = "Nominal")
    private int nominal;

    @Element(name = "ParentCode")
    private String parentCode;

}
