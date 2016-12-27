package com.alekseyorlov.luna.liquibase.producer;

import javax.sql.DataSource;

import liquibase.Contexts;

public interface LiquibaseProducer {

    DataSource liquibaseDataSource();
    
    Contexts liquibaseContexts();
    
    String liquibaseChangeLogFile();
    
}
