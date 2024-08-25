package com.fabbroniko.environment;

import java.util.ArrayDeque;

import static java.util.Objects.requireNonNull;

public class CircularState<T> implements BiDirectionalState<T> {

    private final ArrayDeque<T> states;
    private T currentState;

    public CircularState(ArrayDeque<T> states) {
        this.states = states;
        this.currentState = states.peekFirst();
    }

    @Override
    public T next() {
        states.addLast(requireNonNull(states.pollFirst()));
        currentState = states.peekFirst();
        return currentState;
    }

    @Override
    public T getCurrent() {
        return currentState;
    }

    @Override
    public T previous() {
        states.addFirst(requireNonNull(states.pollLast()));
        currentState = states.peekFirst();
        return currentState;
    }
}
