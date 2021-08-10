package org.example.fff.server;

public interface LifeCycle {
    void start() throws Exception;

    void stop() throws Exception;

    abstract class AbstractLifeCycle implements LifeCycle {

        private State state;

        @Override
        public void start() {
            state = State.STARTING;
            doStart();
            state = State.STARTED;
        }

        @Override
        public void stop(){
            state = State.STOPPING;
            doStop();
            state = State.STOPPED;
        }

        public abstract void doStart();

        public abstract void doStop();
    }

    enum State {
        STOPPED,
        STOPPING,
        STARTED,
        STARTING
    }
}


