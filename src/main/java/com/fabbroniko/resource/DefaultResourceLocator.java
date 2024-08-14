package com.fabbroniko.resource;

import java.net.URL;

public class DefaultResourceLocator implements ResourceLocator {

    @Override
    public URL locate(final String path) {
        return getClass().getResource(path);
    }
}
