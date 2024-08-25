package com.fabbroniko.scene.mainmenu;

import java.awt.Color;

public interface MenuOptionFactory {

    MenuOption create(final String text, final Color color);
}
