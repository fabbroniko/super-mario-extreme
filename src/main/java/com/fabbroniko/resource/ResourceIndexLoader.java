package com.fabbroniko.resource;

import com.fabbroniko.resource.dto.Resource;

public interface ResourceIndexLoader {

    Resource load(final String path);
}
