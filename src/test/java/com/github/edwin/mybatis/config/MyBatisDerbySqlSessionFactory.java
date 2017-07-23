package com.github.edwin.mybatis.config;

import java.io.IOException;
import java.io.Reader;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 * <pre>
 *  com.github.edwin.mybatis.config.MyBatisDerbySqlSessionFactory
 * </pre>
 *
 * @author edwin < edwinkun at gmail dot com >
 * Jul 23, 2017 2:10:22 PM
 *
 */
public class MyBatisDerbySqlSessionFactory {

    private static final SqlSessionFactory FACTORY;

    static {
        try {
            Reader reader = Resources.getResourceAsReader("ConfigurationDerby.xml");
            FACTORY = new SqlSessionFactoryBuilder().build(reader);
        } catch (IOException e) {
            throw new RuntimeException("Fatal Error. Cause: " + e, e);
        }
    }

    public static SqlSessionFactory getSqlSessionFactory() {
        return FACTORY;
    }
}
