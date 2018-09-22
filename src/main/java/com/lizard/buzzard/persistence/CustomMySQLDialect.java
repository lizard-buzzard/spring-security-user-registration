package com.lizard.buzzard.persistence;

import org.hibernate.dialect.MySQL57Dialect;

public class CustomMySQLDialect extends MySQL57Dialect {
    @Override
    public boolean dropConstraints() {
        return false;
    }
}
