package com.github.alex4790354.general.dtoxml.metalResponse;


import lombok.Data;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import java.util.List;

@Root(name = "Metall")
@Data
public class MetalRateXml {

    @Attribute(name = "FromDate")
    private String fromDate;

    @Attribute(name = "ToDate")
    private String toDate;

    @Attribute(name = "name")
    private String name;

    @ElementList(inline = true, entry = "Record")
    private List<MetalRecord> metalRecords;

}
