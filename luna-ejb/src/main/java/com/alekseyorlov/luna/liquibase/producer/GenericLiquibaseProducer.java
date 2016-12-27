package com.alekseyorlov.luna.liquibase.producer;

import javax.annotation.Resource;
import javax.enterprise.context.Dependent;
import javax.sql.DataSource;

import liquibase.Contexts;

@Dependent
public class GenericLiquibaseProducer extends AbstractLiquibaseProducer {

    @Resource
    private DataSource dataSource;
    
    @Override
    public DataSource liquibaseDataSource() {
        
        return dataSource;
    }

    @Override
    public Contexts liquibaseContexts() {
        
        return new Contexts("default");
    }

}