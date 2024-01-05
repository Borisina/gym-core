package com.kolya.gym.config;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

import java.util.Locale;

public class SnakeCaseNamingStrategy implements PhysicalNamingStrategy {
    @Override
    public Identifier toPhysicalCatalogName(final Identifier name, final JdbcEnvironment context) {
        return name;
    }

    @Override
    public Identifier toPhysicalColumnName(final Identifier name, final JdbcEnvironment context) {
        return convertToSnakeCase(name);
    }

    @Override
    public Identifier toPhysicalSchemaName(final Identifier name, final JdbcEnvironment context) {
        return name;
    }

    @Override
    public Identifier toPhysicalSequenceName(final Identifier name, final JdbcEnvironment context) {
        return convertToSnakeCase(name);
    }

    @Override
    public Identifier toPhysicalTableName(final Identifier name, final JdbcEnvironment context) {
        return convertToSnakeCase(name);
    }

    private Identifier convertToSnakeCase(final Identifier id) {
        final String regex = "([a-z])([A-Z])";
        final String replacement = "$1_$2";
        final String newName = id.getText().replaceAll(regex, replacement).toLowerCase(Locale.ROOT);
        return Identifier.toIdentifier(newName);
    }
}