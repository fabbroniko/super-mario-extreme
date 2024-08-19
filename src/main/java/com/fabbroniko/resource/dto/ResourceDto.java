package com.fabbroniko.resource.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JacksonXmlRootElement(localName = "resource")
public class ResourceDto {

    private List<ClipDto> clips;
    private List<BackgroundDto> backgrounds;
    private TileMapDto tileMap;
}
