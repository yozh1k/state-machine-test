package com.zaptain.rnd;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class TransactionChecker{
    private final JdbcTemplate jdbcTemplate;
    private final List<String> transactions;

    @Autowired
    public TransactionChecker(final JdbcTemplate jdbcTemplate) {
        transactions = new CopyOnWriteArrayList<>();
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public void checkTransactionId() {
        transactions.add(
                jdbcTemplate.queryForObject("SELECT txid_current()", String.class)
        );
    }
    public List<String> getTransactions(){
        return Collections.unmodifiableList(transactions);
    }
}
