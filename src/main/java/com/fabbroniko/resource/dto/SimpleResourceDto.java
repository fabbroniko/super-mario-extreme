package com.fabbroniko.resource.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SimpleResourceDto {

    @JacksonXmlProperty(isAttribute = true)
    private String name;

    @JacksonXmlText
    private String path;
}
