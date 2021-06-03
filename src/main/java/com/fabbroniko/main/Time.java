package com.fabbroniko.main;

public class Time {

    private static double deltaTime, cumulativeDeltas;
    private static long variableYieldTime, lastTime;
    private static int fps, frameCount;

    public static double deltaTime() {
        return deltaTime;
    }

    /**
     * Far from precise fps count but good enough to display on-screen.
     * @return The current FPS the game is running on
     */
    public static int getFps() {
        return fps;
    }

    /**
     * A modified version from the LWJGL Library used to keep the fps at the desired value, calculate the delta time used
     * for movements and the actual fps rate the game has been running on.
     *
     * An accurate sync method that adapts automatically
     * to the system it runs on to provide reliable results.
     *
     * @param desiredFps The desired frame rate, in frames per second
     * @author kappa (On the LWJGL Forums)
     */
    public static void sync(final int desiredFps) {
        if (desiredFps <= 0) return;

        long sleepTime = 1000000000 / desiredFps; // nanoseconds to sleep this frame
        // yieldTime + remainder micro & nano seconds if smaller than sleepTime
        long yieldTime = Math.min(sleepTime, variableYieldTime + sleepTime % (1000*1000));
        long overSleep = 0; // time the sync goes over by

        try {
            while (true) {
                long t = System.nanoTime() - lastTime;

                if (t < sleepTime - yieldTime) {
                    Thread.sleep(1);
                }else if (t < sleepTime) {
                    // burn the last few CPU cycles to ensure accuracy
                    Thread.yield();
                }else {
                    overSleep = t - sleepTime;
                    break; // exit while loop
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally{
            final long nanoTime = System.nanoTime();
            deltaTime = ((double)(nanoTime - lastTime))/1000000000;
            lastTime = nanoTime - Math.min(overSleep, sleepTime);

            cumulativeDeltas += deltaTime;
            frameCount++;

            /*
             * Imagine the following scenario:
             * We get a cumulative delta of 0.96 with fps count of 60 (0.016s for each frame) but the next frame takes
             * 200 ms to process for some reason. This would result in a cumulative delta of 1.196 with fps count of 61.
             * A good measure would be to recalculate the fps using 61/1.196 instead of approximating it to whatever fps count we got by simply increasing the counter.
             */
            if(cumulativeDeltas > 1) {
                fps = (int)Math.round(frameCount / cumulativeDeltas);
                cumulativeDeltas = 0;
                frameCount = 0;
            }

            // auto tune the time sync should yield
            if (overSleep > variableYieldTime) {
                // increase by 200 microseconds (1/5 a ms)
                variableYieldTime = Math.min(variableYieldTime + 200*1000, sleepTime);
            } else if (overSleep < variableYieldTime - 200*1000) {
                // decrease by 2 microseconds
                variableYieldTime = Math.max(variableYieldTime - 2*1000, 0);
            }
        }
    }
}
