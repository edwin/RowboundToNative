package com.github.edwin.mybatis.dialect;

/**
 *
 * @author edwin < edwinkun at gmail dot com >
 * Jul 15, 2017 10:13:31 PM
 *
 */
public class OracleSQLDialect extends Dialect {

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
        if (offset > 0) {
            return "select * from ( select row_.*, rownum rownum_ from (" + sql + " ) row_ ) where rownum_ <= " + (offset + limit) + " and rownum_ > " + offset;
        } else {
            return "select * from (" + sql + ") where rownum <= " + limit;
        }
    }

}
