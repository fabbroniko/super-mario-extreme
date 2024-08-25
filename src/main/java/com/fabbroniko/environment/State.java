package com.fabbroniko.environment;

public interface State<T> {

    T next();

    T getCurrent();
}
