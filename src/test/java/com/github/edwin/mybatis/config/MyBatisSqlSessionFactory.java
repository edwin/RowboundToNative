package com.github.edwin.mybatis.config;

import java.io.IOException;
import java.io.Reader;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 * <pre>
 *  com.github.edwin.mybatis.config.MyBatisSqlSessionFactory
 * </pre>
 *
 * @author edwin < edwinkun at gmail dot com >
 * Jul 15, 2017 11:44:31 PM
 *
 */
public class MyBatisSqlSessionFactory {

    private static final SqlSessionFactory FACTORY;

    static {
        try {
            Reader reader = Resources.getResourceAsReader("ConfigurationMySql.xml");
            FACTORY = new SqlSessionFactoryBuilder().build(reader);
        } catch (IOException e) {
            throw new RuntimeException("Fatal Error. Cause: " + e, e);
        }
    }

    public static SqlSessionFactory getSqlSessionFactory() {
        return FACTORY;
    }
}
