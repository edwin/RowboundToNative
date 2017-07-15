package com.github.edwin.mybatis.dialect;

/**
 *
 * @author edwin < edwinkun at gmail dot com >
 * Feb 3, 2017 12:59:35 AM
 *
 */
public class MySQLDialect extends Dialect {

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
            return sql + " limit " + offset + "," + limit;
        return sql + " limit " + limit;
    }

}
