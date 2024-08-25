package com.fabbroniko.scene.mainmenu;

import com.fabbroniko.environment.CircularState;

public interface StateFactory<T> {

    CircularState<T> create();
}
