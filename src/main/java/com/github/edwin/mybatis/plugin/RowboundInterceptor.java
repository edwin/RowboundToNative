/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.edwin.mybatis.plugin;

import com.github.edwin.mybatis.dialect.Dialect;
import com.github.edwin.mybatis.session.RowBounds2;
import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.MappedStatement.Builder;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import org.apache.ibatis.mapping.ParameterMapping;

/**
 *
 * @author edwin < edwinkun at gmail dot com >
 * Jul 15, 2017 10:06:28 PM
 *
 */
@Intercepts({
    @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class,
        RowBounds.class, ResultHandler.class})})
public class RowboundInterceptor implements Interceptor {

    static int MAPPED_STATEMENT_INDEX = 0;

    static int PARAMETER_INDEX = 1;

    static int ROWBOUNDS_INDEX = 2;

    static int RESULT_HANDLER_INDEX = 3;

    private Dialect dialect;

    @Override
    public Object intercept(Invocation invocation)
            throws Throwable {
        processIntercept(invocation.getArgs());
        return invocation.proceed();
    }

    void processIntercept(final Object[] queryArgs) {
        MappedStatement ms = (MappedStatement) queryArgs[MAPPED_STATEMENT_INDEX];
        Object parameter = queryArgs[PARAMETER_INDEX];
        final Object rowBounds = queryArgs[ROWBOUNDS_INDEX];
        long offset = 0;
        long limit = 0;
        
        // check whether its rowbound2 or a simple rowbound
        boolean isRowbound2 = (rowBounds instanceof RowBounds2);
        if(isRowbound2) {
            offset = ((RowBounds2)rowBounds).getOffset2();
            limit = ((RowBounds2)rowBounds).getLimit2();
        } else {
            offset = ((RowBounds)rowBounds).getOffset();
            limit = ((RowBounds)rowBounds).getLimit();
        }
        
        if (dialect.supportsLimit() && (offset != RowBounds.NO_ROW_OFFSET || limit != RowBounds.NO_ROW_LIMIT)) {
            BoundSql boundSql = ms.getBoundSql(parameter);
            String sql = boundSql.getSql().trim();

            if (dialect.supportsLimitOffset()) {
                sql = dialect.getLimitString(sql, offset, limit);
                offset = RowBounds.NO_ROW_OFFSET;
            } else {
                sql = dialect.getLimitString(sql, 0, limit);
            }
            limit = RowBounds.NO_ROW_LIMIT;

            if(isRowbound2) {
                queryArgs[ROWBOUNDS_INDEX] = new RowBounds2(offset, limit);
            } else if(rowBounds instanceof RowBounds) {
                queryArgs[ROWBOUNDS_INDEX] = new RowBounds((int)offset, (int)limit);
            }
            
            BoundSql newBoundSql
                    = copyFromBoundSql(ms, sql, boundSql, boundSql.getParameterMappings(), parameter);
            MappedStatement newMs = copyFromMappedStatement(ms, new BoundSqlSqlSource(newBoundSql));
            queryArgs[MAPPED_STATEMENT_INDEX] = newMs;
        }
    }

    private BoundSql copyFromBoundSql(MappedStatement ms, String sql, BoundSql boundSql,
            List<ParameterMapping> parameterMappings, Object parameter) {
        BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), sql, parameterMappings, parameter);
        for (ParameterMapping mapping : boundSql.getParameterMappings()) {
            String prop = mapping.getProperty();
            if (boundSql.hasAdditionalParameter(prop)) {
                newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
            }
        }
        return newBoundSql;
    }

    private MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
        Builder builder
                = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());

        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());

        return builder.build();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        String dialectClass = properties.get("dialectClass").toString();
        try {
            dialect = (Dialect) Class.forName(dialectClass).newInstance();
        } catch (Exception e) {
            throw new RuntimeException("cannot create dialect instance by dialectClass:" + dialectClass, e);
        }
    }

    public static class BoundSqlSqlSource implements SqlSource {

        BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }

}
