package com.fabbroniko;

import com.fabbroniko.main.GameManager;
import com.fabbroniko.sdi.DependencyInjector;
import com.fabbroniko.sdi.annotation.Configuration;
import com.fabbroniko.ul.manager.Log4jLogManager;

@Configuration(componentScan = "com.fabbroniko", logger = Log4jLogManager.class)
public class Application {

    public static void main(final String[] args) {
        DependencyInjector.run(Application.class).<GameManager>getInstance(GameManager.class).start();
    }
}
