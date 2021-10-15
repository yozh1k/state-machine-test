package com.zaptain.rnd;

import com.zaptain.common.Event;
import com.zaptain.common.State;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;

@EnableStateMachine
@Configuration
public class SmListenerConfig {
    @Bean
    public StateMachine<State, Event> stateMachine() throws Exception {
        StateMachineBuilder.Builder<State, Event> builder = StateMachineBuilder.builder();
        builder.configureStates().withStates()
                .initial(State.ALPHA)
                .end(State.OMEGA);
        builder.configureTransitions()
                .withExternal()
                .source(State.ALPHA).target(State.OMEGA).event(Event.GO)
                .action(
                        FailedAction.instance()
                );
        return builder.build();
    }

    @Bean
    public StateMachineListener<State, Event> stateMachineEventListener(StateMachine<State, Event> stateMachine) {
        final StateMachineListenerAdapter<State, Event> adapter = new StateMachineListenerAdapter<>();
        return adapter;
    }
}
