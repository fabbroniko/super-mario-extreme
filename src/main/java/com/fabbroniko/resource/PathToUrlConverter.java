package com.fabbroniko.resource;

import java.net.URL;

public interface PathToUrlConverter {

    URL locate(final String path);
}
