package com.zaptain.rnd;

import com.zaptain.common.Event;
import com.zaptain.common.State;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.test.StateMachineTestPlanBuilder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class StateMachineErrorHandlingExtraTest {

    /**
     * Тест демонстрирует, машина не меняет состояние, при этом блок обработки ошибок interceptor'а не вызывается
     */
    @Test
    public void testEmptyInterceptor() throws Exception {
        var builder = StateMachineBuilder.<State, Event>builder();
        builder.configureStates().withStates()
                .initial(State.ALPHA)
                .end(State.OMEGA);
        builder.configureTransitions()
                .withExternal()
                .source(State.ALPHA).target(State.OMEGA).event(Event.GO)
                .action(
                        FailedAction.instance()
                );
        final EmptyErrorInterceptor interceptor = Mockito.spy(new EmptyErrorInterceptor());
        final StateMachine<State, Event> stateMachine = builder.build();
        stateMachine.getStateMachineAccessor()
                .doWithRegion(sma -> sma.addStateMachineInterceptor(interceptor));

        var plan = StateMachineTestPlanBuilder.<State, Event>builder()
                .defaultAwaitTime(2)
                .stateMachine(stateMachine)
                .step()
                .expectStates(State.ALPHA)
                .and().step()
                .sendEvent(Event.GO)
                .expectState(State.ALPHA)
                .and()
                .build();
        plan.test();
        verify(interceptor, never()).stateMachineError(any(), any());
    }

    /**
     * Тест демонстрирует, машина не меняет состояние, при этом блок обработки ошибок interceptor'а не вызывается
     */
    @Test
    public void testSwallowInterceptor() throws Exception {
        var builder = StateMachineBuilder.<State, Event>builder();
        builder.configureStates().withStates()
                .initial(State.ALPHA)
                .end(State.OMEGA);
        builder.configureTransitions()
                .withExternal()
                .source(State.ALPHA).target(State.OMEGA).event(Event.GO)
                .action(
                        FailedAction.instance()
                );
        final SwallowErrorInterceptor interceptor = Mockito.spy(new SwallowErrorInterceptor());
        final StateMachine<State, Event> stateMachine = builder.build();
        stateMachine.getStateMachineAccessor()
                .doWithRegion(sma -> sma.addStateMachineInterceptor(interceptor));

        var plan = StateMachineTestPlanBuilder.<State, Event>builder()
                .defaultAwaitTime(2)
                .stateMachine(stateMachine)
                .step()
                .expectStates(State.ALPHA)
                .and().step()
                .sendEvent(Event.GO)
                .expectState(State.ALPHA)
                .and()
                .build();
        plan.test();
        verify(interceptor, never()).stateMachineError(any(), any());
    }

}
