package org.smvc.framework.orm.annotation;

import java.util.ArrayList;
import java.util.List;

/**
 * package sql and sql's parameter
 * 
 */
public class SqlHolder {
    private String sql;
    private List<Object> params = new ArrayList<Object>();

    public void addParam(Object o) {
        params.add(o);
    }

    public Object[] getParams() {
        return params.toArray();
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("executed sql:");
        sb.append(sql).append("\r\n").append("             parameter:");
        for (Object obj : params) {
            sb.append(obj).append(",");
        }
        if (params.size() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }
}
