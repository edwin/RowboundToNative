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
