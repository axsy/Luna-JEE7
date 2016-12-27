package com.alekseyorlov.luna.liquibase.producer;

import javax.annotation.Resource;
import javax.sql.DataSource;

public abstract class AbstractTestLiquibaseProducer extends AbstractLiquibaseProducer {

    @Resource(lookup = "java:app/Luna/TestDS")
    private DataSource dataSource;
    
    @Override
    public DataSource liquibaseDataSource() {
        
        return dataSource;
    }
    
}
