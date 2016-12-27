package com.alekseyorlov.luna.liquibase.producer;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;
import javax.sql.DataSource;

import com.alekseyorlov.luna.liquibase.annotation.LiquibaseType;

import liquibase.Contexts;

@Dependent
@Alternative
public class GenericTestLiquibaseProducer extends AbstractTestLiquibaseProducer {

    @Override
    @Produces
    @LiquibaseType
    public Contexts liquibaseContexts() {
        
        return new Contexts("default");
    }

    @Override
    @Produces
    @LiquibaseType
    public DataSource liquibaseDataSource() {

        return super.liquibaseDataSource();
    }

    @Override
    @Produces
    @LiquibaseType
    public String liquibaseChangeLogFile() {

        return super.liquibaseChangeLogFile();
    }
    
}
