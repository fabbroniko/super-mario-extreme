package com.fabbroniko.resource.dto;

import com.fabbroniko.environment.Vector2D;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@JacksonXmlRootElement
@JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
public class LevelDto {

  @JacksonXmlElementWrapper(localName = "game-objects")
  @JacksonXmlProperty(localName = "game-object")
  private List<GameObjectDto> gameObjects = new ArrayList<>();

  private MapDto map;
  private Vector2D startPosition;

  @JacksonXmlProperty(isAttribute = true)
  private double requiredGameVersion;
}
