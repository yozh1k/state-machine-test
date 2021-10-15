package com.zaptain.rnd;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

public class FailedAction implements Action<State, Event> {
    public static Action<State, Event> instance() {
        return new FailedAction();
    }

    @Override
    public void execute(final StateContext<State, Event> context) {
        throw new RuntimeException("action failed");
    }

}
