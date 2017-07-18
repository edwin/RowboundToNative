package com.github.edwin.mybatis.session;

import org.apache.ibatis.session.RowBounds;

/**
 * <pre>
 *  com.github.edwin.mybatis.session.RowBounds2 
 * </pre>
 *
 * @author edwin < edwinkun at gmail dot com >
 * Jul 18, 2017 9:33:54 PM
 *
 */
public class RowBounds2 extends RowBounds {

    private long offset2 = 0l;
    private long limit2 = 0l;
    
    public RowBounds2() {
    }

    public RowBounds2(int offset2, int limit2) {
        super(offset2, limit2);
    }

    public RowBounds2(long offset2, long limit2) {
        this.offset2 = offset2;
        this.limit2 = limit2;
    }

    public long getOffset2() {
        return offset2;
    }

    public void setOffset2(long offset2) {
        this.offset2 = offset2;
    }

    public long getLimit2() {
        return limit2;
    }

    public void setLimit2(long limit2) {
        this.limit2 = limit2;
    }
}
