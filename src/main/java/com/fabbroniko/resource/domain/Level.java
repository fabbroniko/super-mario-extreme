package com.fabbroniko.resource.domain;

import com.fabbroniko.environment.Position;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JacksonXmlRootElement
public class Level {

  @JacksonXmlElementWrapper(localName = "game-objects")
  @JacksonXmlProperty(localName = "game-object")
  private List<GameObject> gameObjects;
  private Map map;

  @JacksonXmlProperty(localName = "start-position")
  private Position startPosition;

  @JacksonXmlProperty(isAttribute = true, localName = "required-game-version")
  private double requiredGameVersion;
}