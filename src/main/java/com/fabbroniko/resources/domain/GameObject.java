package com.fabbroniko.resources.domain;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GameObject {

  @JacksonXmlProperty(isAttribute = true)
  private int x;

  @JacksonXmlProperty(isAttribute = true)
  private int y;

  @JacksonXmlText
  private String type;
}
