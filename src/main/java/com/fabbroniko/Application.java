package com.fabbroniko;

import com.fabbroniko.main.GameManager;
import org.example.DependencyInjector;
import org.example.annotation.Configuration;

@Configuration(componentScan = "com.fabbroniko")
public class Application {

    public static void main(final String[] args) {
        DependencyInjector.run(Application.class).<GameManager>getInstance(GameManager.class).start();
    }
}
