package com.github.edwin.mybatis.dialect;

/**
 * 
 * @author edwin < edwinkun at gmail dot com >
 * Jun 4, 2017 8:32:52 PM
 *
 */
public class PostgreSQLDialect extends Dialect {

    @Override
    public boolean supportsLimitOffset() {
        return true;
    }

    @Override
    public boolean supportsLimit() {
        return true;
    }

    @Override
    public String getLimitString(String sql, int offset, int limit) {
        if (offset > 0) 
            return sql + " limit " + limit + " offset " + offset;
        return sql + " limit " + limit;
    }
}
