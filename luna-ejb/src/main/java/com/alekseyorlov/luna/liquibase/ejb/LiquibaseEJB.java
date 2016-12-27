package com.alekseyorlov.luna.liquibase.ejb;

import java.sql.SQLException;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.sql.DataSource;

import com.alekseyorlov.luna.liquibase.annotation.LiquibaseType;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

@Singleton
@Startup
public class LiquibaseEJB {

    @SuppressWarnings("cdi-ambiguous-dependency")
    @Inject
    @LiquibaseType
    private DataSource dataSource;
    
    @SuppressWarnings("cdi-ambiguous-dependency")
    @Inject
    @LiquibaseType
    private Contexts contexts;
    
    @SuppressWarnings("cdi-ambiguous-dependency")
    @Inject
    @LiquibaseType
    private String changeLogFile;
    
    @PostConstruct
    private void initialize() {
        try {
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(
                    new JdbcConnection(dataSource.getConnection()));
            Liquibase liquibase = new Liquibase(
                    changeLogFile,
                    new ClassLoaderResourceAccessor(getClass().getClassLoader()),
                    database);
            
            liquibase.update(contexts);
        } catch (SQLException | LiquibaseException e) {
            throw new IllegalStateException(e);
        }
    }
}
