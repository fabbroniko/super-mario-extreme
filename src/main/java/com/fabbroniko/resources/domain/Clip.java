package com.fabbroniko.resources.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Clip {

    private String name;
    private String type;
    private boolean preload;
    private boolean loop;
    private String path;
}
