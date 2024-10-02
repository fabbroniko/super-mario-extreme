package com.fabbroniko.resource;

import java.util.Optional;

public interface ResourceCache<T> {

    Optional<T> findByName(final String name);

    void insert(final String name, final T resource);
}
