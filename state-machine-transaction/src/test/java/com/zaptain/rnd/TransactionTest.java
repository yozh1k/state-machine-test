package com.zaptain.rnd;

import com.zaptain.common.Event;
import com.zaptain.common.State;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureJdbc;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import reactor.core.publisher.Mono;

import javax.sql.DataSource;
import java.util.List;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {SmTransactionConfig.class, TransactionTest.DSConfig.class})
public class TransactionTest {
    @Autowired
    StateMachine<State, Event> stateMachine;
    @Autowired
    TransactionChecker transactionChecker;

    @Test
    @Transactional
    public void test() {
        stateMachine.startReactively().block();
        transactionChecker.checkTransactionId();
        stateMachine.sendEvent(
                Mono.just(MessageBuilder.withPayload(Event.GO).build())
        ).blockLast().complete();
        final List<String> transactions = transactionChecker.getTransactions();
        Assert.assertEquals(2, transactions.size());
        Assert.assertEquals(transactions.get(0), transactions.get(1));
    }

    @TestConfiguration
    @Configuration
    @AutoConfigureJdbc
    public static class DSConfig {
        @Bean(initMethod = "start", destroyMethod = "stop")
        public JdbcDatabaseContainer<?> jdbcDatabaseContainer() {
            return new PostgreSQLContainer<>("postgres:11")
                    .waitingFor(Wait.forListeningPort());
        }
        @Bean
        public DataSource dataSource(JdbcDatabaseContainer<?> jdbcDatabaseContainer) {
            var dataSource = new PGSimpleDataSource();
            dataSource.setURL(jdbcDatabaseContainer.getJdbcUrl());
            dataSource.setUser(jdbcDatabaseContainer.getUsername());
            dataSource.setPassword(jdbcDatabaseContainer.getPassword());
            return dataSource;
        }
    }

}
