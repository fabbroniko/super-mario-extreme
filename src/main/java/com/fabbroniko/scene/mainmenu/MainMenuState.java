package com.fabbroniko.scene.mainmenu;

import java.util.List;

public interface MainMenuState {

    void onConfirm();

    List<MenuOption> getOptions();
}
