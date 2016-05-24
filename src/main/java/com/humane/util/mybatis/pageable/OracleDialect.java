package com.humane.util.mybatis.pageable;


public class OracleDialect extends Dialect {
    final String LIMIT_SQL_PATTERN = "select * from ( select row__.*, rownum rownum__ from ( %s ) row__ where rownum <=  %s ) where rownum__ > %s ";
    final String LIMIT_SQL_PATTERN_FIRST = "select * from ( %s ) where rownum <= %s";

    @Override
    public String getLimitString(String sql, int offset, int limit) {
        sql = sql.trim();
        sql.replaceAll("for\\s+update", "");

        if (offset == 0) return String.format(LIMIT_SQL_PATTERN_FIRST, sql, limit);
        return String.format(LIMIT_SQL_PATTERN, sql, offset + limit, offset);
    }
}

