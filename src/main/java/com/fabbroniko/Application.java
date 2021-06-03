package com.fabbroniko;

public class Application {

    /**
     * Entry point, it starts the GameManager and the game loop.
     * @param args No arguments are used for this application.
     */
    public static void main(final String[] args) {
        GameManager.getInstance().start();
    }
}
