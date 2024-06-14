package com.github.alex4790354.general.dtoxml.valcursHistResponse;


import lombok.Data;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import java.util.List;

@Root(name = "ValCurs")
@Data
public class ValCursHist {

    @Attribute(name = "ID")
    private String id;

    @Attribute(name = "DateRange1")
    private String dateRange1;

    @Attribute(name = "DateRange2")
    private String dateRange2;

    @Attribute(name = "name")
    private String name;

    @ElementList(inline = true, entry = "Record")
    private List<ValRecord> valRecords;

}
