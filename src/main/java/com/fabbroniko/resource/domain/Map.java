package com.fabbroniko.resource.domain;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
public class Map {

  private int verticalBlocks;
  private int horizontalBlocks;

  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "tile")
  final List<Tile> tiles = new ArrayList<>();
}
