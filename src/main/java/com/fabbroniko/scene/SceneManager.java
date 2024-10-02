package com.fabbroniko.scene;

public interface SceneManager {

    void openScene(final Class<? extends Scene> scene);

    void quit();
}
