package com.zaptain.rnd;

import com.zaptain.common.Event;
import com.zaptain.common.State;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;

/**
 * just do nothing with error
 */
public class EmptyErrorInterceptor extends StateMachineInterceptorAdapter<State, Event> {
    @Override
    public Exception stateMachineError(final StateMachine<State, Event> stateMachine, final Exception exception) {
        return super.stateMachineError(stateMachine, exception);
    }
}
