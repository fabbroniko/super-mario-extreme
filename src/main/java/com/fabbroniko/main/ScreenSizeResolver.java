package com.fabbroniko.main;

import java.awt.Dimension;
import java.awt.Toolkit;

public class ScreenSizeResolver implements WindowSizeResolver {

    @Override
    public Dimension dimension() {
        return Toolkit.getDefaultToolkit().getScreenSize();
    }
}
