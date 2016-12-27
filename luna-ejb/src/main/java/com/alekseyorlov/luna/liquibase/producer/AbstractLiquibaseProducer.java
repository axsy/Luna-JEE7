package com.alekseyorlov.luna.liquibase.producer;

public abstract class AbstractLiquibaseProducer implements LiquibaseProducer {

    static final String LIQUIBASE_CHANGE_LOG_FILE = "liquibase/db.changelog.xml";
    
    @Override
    public String liquibaseChangeLogFile() {
        
        return LIQUIBASE_CHANGE_LOG_FILE;
    }
}
