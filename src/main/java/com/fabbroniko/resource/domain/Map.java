package com.fabbroniko.resource.domain;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Map {

  @JacksonXmlProperty(localName = "vertical-blocks")
  private int verticalBlocks;
  @JacksonXmlProperty(localName = "horizontal-blocks")
  private int horizontalBlocks;

  @JacksonXmlElementWrapper(useWrapping = false)
  final List<Tile> tile = new ArrayList<>();
}
