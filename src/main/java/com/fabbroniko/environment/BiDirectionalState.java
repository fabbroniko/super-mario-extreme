package com.fabbroniko.environment;

public interface BiDirectionalState<T> extends State<T> {

    T previous();
}
