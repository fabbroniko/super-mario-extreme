package com.fabbroniko.environment;

/**
 * An {@link Animation Animation} can be created passing a listener object implementing this interface. This is used from the
 * Animation class to provide callbacks to listeners when a particular event is triggered.
 */
public interface AnimationListener {

    /**
     *  Callback method called when an animation reached it's final frame.
     */
    void animationFinished();
}
