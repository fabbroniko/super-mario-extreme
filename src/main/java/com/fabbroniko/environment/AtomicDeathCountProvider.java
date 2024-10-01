package com.fabbroniko.environment;

import com.fabbroniko.sdi.annotation.Component;
import com.fabbroniko.ul.Logger;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class AtomicDeathCountProvider implements DeathCountProvider {

    private final AtomicInteger counter = new AtomicInteger(1);

    @Override
    public int getAndIncrement() {
        return counter.getAndIncrement();
    }
}
