package com.fabbroniko.scene;

import com.fabbroniko.gameobjects.GameObject;

import java.util.List;

public interface GameScene extends Scene {

    List<GameObject> gameObjects();
}
