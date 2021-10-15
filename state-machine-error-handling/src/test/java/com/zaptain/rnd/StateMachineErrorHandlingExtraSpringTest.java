package com.zaptain.rnd;

import com.zaptain.common.Event;
import com.zaptain.common.State;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.test.StateMachineTestPlanBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Тест демонстрирует, машина не меняет состояние, при этом блок обработки ошибок listener'а не вызывается
 */
@RunWith( SpringRunner.class )
@ContextConfiguration(classes = SmListenerConfig.class)
@EnableStateMachine
public class StateMachineErrorHandlingExtraSpringTest {
    @Autowired
    StateMachine<State, Event> stateMachine;
    @Spy
    StateMachineListener<State, Event> stateMachineEventListener;

    @Test
    public void testErrorEventListener() throws Exception {
        stateMachine.addStateListener(stateMachineEventListener);
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
        verify(stateMachineEventListener, never()).stateMachineError(any(), any());
        verify(stateMachineEventListener).stateMachineStarted(any());
    }


}
