package com.fabbroniko.resource;

import java.io.InputStream;

public interface ResourceLoader {

    InputStream load(final String path);
}
