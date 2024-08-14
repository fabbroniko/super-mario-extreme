package com.fabbroniko.resource;

import java.net.URL;

public interface ResourceLocator {

    URL locate(final String path);
}
