package com.fabbroniko.resource.domain;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JacksonXmlRootElement(localName = "resource")
public class Resource {

    private List<Clip> clips;
    private List<Background> backgrounds;
    private TileMap tilemap;
}
