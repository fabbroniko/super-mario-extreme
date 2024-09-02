package com.fabbroniko.resource;

import org.example.annotation.Component;

import java.net.URL;

@Component
public class DefaultPathToUrlConverter implements PathToUrlConverter {

    @Override
    public URL locate(final String path) {
        return getClass().getResource(path);
    }
}
