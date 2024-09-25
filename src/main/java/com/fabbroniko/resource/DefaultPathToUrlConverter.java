package com.fabbroniko.resource;

import com.fabbroniko.sdi.annotation.Component;

import java.net.URL;

@Component
public class DefaultPathToUrlConverter implements PathToUrlConverter {

    @Override
    public URL locate(final String path) {
        return getClass().getResource(path);
    }
}
