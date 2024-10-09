package com.fabbroniko.main;

import com.fabbroniko.sdi.annotation.Component;

import java.awt.Dimension;
import java.awt.Toolkit;

@Component("screenSizeResolver")
public class ScreenSizeResolver implements WindowSizeResolver {

    @Override
    public Dimension dimension() {
        return Toolkit.getDefaultToolkit().getScreenSize();
    }
}
