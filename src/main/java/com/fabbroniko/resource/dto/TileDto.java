package com.fabbroniko.resource.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
public class TileDto {

    private int id;
    private int verticalIndex;
    private int horizontalIndex;
}
