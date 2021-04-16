package com.fabbroniko.resources.domain;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TileMap {

  @JacksonXmlProperty(localName = "tile-size")
  private int tileSize;

  @JacksonXmlText
  private String path;
}
