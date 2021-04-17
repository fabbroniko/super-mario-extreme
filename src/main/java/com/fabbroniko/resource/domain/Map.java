package com.fabbroniko.resource.domain;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Map {

  @JacksonXmlText
  private String path;
}
