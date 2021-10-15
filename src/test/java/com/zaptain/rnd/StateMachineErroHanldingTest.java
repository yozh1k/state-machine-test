package com.zaptain.rnd;

import com.zaptain.rnd.Event;
import com.zaptain.rnd.FailedAction;
import com.zaptain.rnd.State;
import org.junit.Test;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.guard.Guard;
import org.springframework.statemachine.test.StateMachineTestPlanBuilder;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * В общем случае для обработки исключений, возникших в процессе работы transition Action'ов без изменения состояния
 * пригоден только Action Handler
 */

public class StateMachineErroHanldingTest {

    /**
     * Тест демонстрирует, что без всякой доп обработки в случае ошибки в процессе работы Action'а,
     * машина не меняет состояние
     */
    @Test
    public void testPure() throws Exception {
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

        var plan = StateMachineTestPlanBuilder.<State, Event>builder()
                .defaultAwaitTime(2)
                .stateMachine(builder.build())
                .step()
                .expectStates(State.ALPHA)
                .and().step()
                .sendEvent(Event.GO)
                .expectState(State.ALPHA)
                .and()
                .build();
        plan.test();
    }

    /**
     * Тест демонстрирует, что в случае обработки ошибки, возникшей в процессе работы Action'а, при помощи
     * error action'а машина не меняет состояние
     */
    @Test
    public void testTransitionActionError() throws Exception {
        var builder = StateMachineBuilder.<State, Event>builder();
        builder.configureStates().withStates()
                .initial(State.ALPHA)
                .end(State.OMEGA);
        builder.configureTransitions()
                .withExternal()
                .source(State.ALPHA).target(State.OMEGA).event(Event.GO)
                .action(
                        FailedAction.instance(),
                        c -> c.getException().printStackTrace()
                );

        var plan = StateMachineTestPlanBuilder.<State, Event>builder()
                .defaultAwaitTime(2)
                .stateMachine(builder.build())
                .step()
                .expectStates(State.ALPHA)
                .and().step()
                .sendEvent(Event.GO)
                .expectState(State.ALPHA)
                .and()
                .build();
        plan.test();
    }

    /**
     * Тест демонстрирует, что в случае обработки ошибки, возникшей в процессе работы Action'а, при помощи
     * Guard'а машина невозможна в общем случае, т.к. Guard срабатывает до Action'ов
     */
    @Test
    public void testTransitionActionErrorAndGuard() throws Exception {
        final Guard guard = mock(Guard.class);
        when(guard.evaluate(any())).thenReturn(false);
        Action<State, Event> failedAction = mock(Action.class);

        var builder = StateMachineBuilder.<State, Event>builder();
        builder.configureStates().withStates()
                .initial(State.ALPHA)
                .end(State.OMEGA);
        builder.configureTransitions()
                .withExternal()
                .source(State.ALPHA).target(State.OMEGA).event(Event.GO)
                .action(
                        failedAction,
                        c -> {
                        }
                ).guard(guard);

        var plan = StateMachineTestPlanBuilder.<State, Event>builder()
                .defaultAwaitTime(2)
                .stateMachine(builder.build())
                .step()
                .expectStates(State.ALPHA)
                .and().step()
                .sendEvent(Event.GO)
                .expectState(State.ALPHA)
                .and()
                .build();
        plan.test();

        verify(failedAction, times(0)).execute(any());
        verify(guard, times(1)).evaluate(any());
    }



}
