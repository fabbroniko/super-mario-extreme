package com.fabbroniko.resource.domain;

import com.fabbroniko.environment.Position;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JacksonXmlRootElement
@JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
public class Level {

  @JacksonXmlElementWrapper(localName = "game-objects")
  @JacksonXmlProperty(localName = "game-object")
  private List<GameObject> gameObjects;

  private Map map;
  private Position startPosition;

  @JacksonXmlProperty(isAttribute = true)
  private double requiredGameVersion;
}
