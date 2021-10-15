package com.zaptain.rnd;

import com.zaptain.common.Event;
import com.zaptain.common.State;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;


@EnableStateMachine
@Configuration
@ComponentScan(basePackageClasses = TransactionChecker.class)
public class SmTransactionConfig {
    @Bean
    public StateMachine<State, Event> stateMachine(TransactionChecker transactionChecker) throws Exception {
        Action<State, Event> action = context -> transactionChecker.checkTransactionId();
        StateMachineBuilder.Builder<State, Event> builder = StateMachineBuilder.builder();
        builder.configureStates().withStates()
                .initial(State.ALPHA)
                .end(State.OMEGA);
        builder.configureTransitions()
                .withExternal()
                .source(State.ALPHA).target(State.OMEGA)
                .action(action)
                .event(Event.GO);
        return builder.build();
    }
}
