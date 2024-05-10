package com.github.alex4790354.general.dtoxml.valutaResponse;

import lombok.Data;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import java.util.List;

@Root(name = "Valuta")
@Data
public class Valuta {

    @Attribute(name = "name")
    private String name;

    @ElementList(inline = true)
    private List<Item> items;

}


