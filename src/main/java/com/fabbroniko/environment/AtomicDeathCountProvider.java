package com.fabbroniko.environment;

import com.fabbroniko.sdi.annotation.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class AtomicDeathCountProvider implements DeathCountProvider {

    private final AtomicInteger counter = new AtomicInteger(1);

    @Override
    public int getAndIncrement() {
        return counter.getAndIncrement();
    }
}
