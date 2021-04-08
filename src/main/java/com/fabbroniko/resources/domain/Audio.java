package com.fabbroniko.resources.domain;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Audio {

    @JacksonXmlElementWrapper(useWrapping = false)
    List<Clip> clip;
}
