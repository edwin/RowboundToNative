package com.github.edwin.mybatis.dialect;

/**
 *
 * @author edwin < edwinkun at gmail dot com >
 * Feb 3, 2017 12:56:21 AM
 *
 */
public class Dialect {

    public boolean supportsLimit() {
        return false;
    }

    public boolean supportsLimitOffset() {
        return supportsLimit();
    }

    public String getLimitString(String sql, int offset, int limit) {
        throw new UnsupportedOperationException("paged queries not supported");
    }
}
