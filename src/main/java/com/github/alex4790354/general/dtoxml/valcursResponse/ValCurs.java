package com.github.alex4790354.general.dtoxml.valcursResponse;


import lombok.Data;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import java.util.List;

@Root(name = "ValCurs")
@Data
public class ValCurs {

    @Attribute(name = "Date")
    private String date;

    @Attribute(name = "name")
    private String name;

    @ElementList(inline = true, entry = "Valute")
    private List<Valute> valutes;

}
