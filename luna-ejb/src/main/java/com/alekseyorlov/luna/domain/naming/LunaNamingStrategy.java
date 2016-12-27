package com.alekseyorlov.luna.domain.naming;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

import java.util.regex.Pattern;

public class LunaNamingStrategy extends PhysicalNamingStrategyStandardImpl {

    private static final long serialVersionUID = -7653786212222169977L;

    public static final String DEFAULT_PREFIX = "luna";

    private static final Pattern CAMELCASE_WORD_BOUNDARY_PATTERN = Pattern.compile("([0-9a-z])([A-Z])");

    private String prefix;

    public LunaNamingStrategy() {
        this(DEFAULT_PREFIX);
    }

    public LunaNamingStrategy(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
        return new Identifier(prefix + "_" + lowerUnderscore(name.getText()), name.isQuoted());
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context) {
        return new Identifier(lowerUnderscore(name.getText()), name.isQuoted());
    }

    private String lowerUnderscore(String text) {
        return CAMELCASE_WORD_BOUNDARY_PATTERN.matcher(text).replaceAll("$1_$2").toLowerCase();
    }
}
