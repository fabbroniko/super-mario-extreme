package com.fabbroniko.resource;

import com.fabbroniko.resource.dto.ResourceDto;

public interface ResourceIndexLoader {

    ResourceDto load(final String path);
}
