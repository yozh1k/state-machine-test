package com.zaptain.rnd;

import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;

/**
 * swallows error
 */
public class SwallowErrorInterceptor  extends StateMachineInterceptorAdapter<State, Event> {
    @Override
    public Exception stateMachineError(final StateMachine<State, Event> stateMachine, final Exception exception) {
        return null;
    }

}
