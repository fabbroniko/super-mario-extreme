package com.fabbroniko.resource;

import java.net.URL;

public class DefaultPathToUrlConverter implements PathToUrlConverter {

    @Override
    public URL locate(final String path) {
        return getClass().getResource(path);
    }
}
