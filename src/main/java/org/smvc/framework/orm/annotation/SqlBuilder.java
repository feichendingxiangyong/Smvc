package org.smvc.framework.orm.annotation;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Build SqlHolder with object
 * 
 */
public class SqlBuilder {
    private final static Logger logger = Logger.getLogger(SqlBuilder.class);

    /**
     * build sqlHolder of inserting an object
     * 
     * @param po
     * @return
     */
    public static SqlHolder buildInsert(Object po) {
        SqlHolder holder = new SqlHolder();
        Field[] fields = po.getClass().getDeclaredFields();

        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();

        for (Field f : fields) {
            if (isTransient(f)) {
                continue;
            }
            holder.addParam(convert(getValue(po, f)));
            columns.append(columnName(f)).append(",");
            values.append("?").append(",");

        }
        deleteLastComma(columns);
        deleteLastComma(values);

        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ").append(tableName(po)).append(" (");
        sql.append(columns).append(") ");
        sql.append(" VALUES(").append(values).append(") ");
        holder.setSql(sql.toString());
        logger.debug(holder);
        return holder;

    }

    /**
     * build sqlHolder of inserting a list of objects. Note: the class of
     * objects must be same!
     * 
     * @param po
     * @return SqlHolder
     */
    public static <T> SqlHolder buildInsertList(List<T> pos) {
        SqlHolder holder = new SqlHolder();
        Field[] fields = pos.get(0).getClass().getDeclaredFields();

        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();

        // build columns' sql
        for (Field f : fields) {
            if (isTransient(f)) {
                continue;
            }
            columns.append(columnName(f)).append(",");
            values.append("?").append(",");

        }
        deleteLastComma(columns);
        deleteLastComma(values);

        // build params
        List<Object> tempParams = new ArrayList<Object>();
        for (Object po : pos) {
            tempParams.clear();
            for (Field f : fields) {
                if (isTransient(f)) {
                    continue;
                }
                tempParams.add(convert(getValue(po, f)));
            }
            holder.addParam(tempParams.toArray());
        }

        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ").append(tableName(pos.get(0))).append(" (");
        sql.append(columns).append(") ");
        sql.append(" VALUES(").append(values).append(") ");
        holder.setSql(sql.toString());
        logger.debug(holder);
        return holder;

    }

    /**
     * build update sql
     * 
     * @param obj
     * @param where
     * @return
     */
    public static SqlHolder buildUpdate(Object obj, String where) {
        SqlHolder holder = new SqlHolder();
        Field[] fields = obj.getClass().getDeclaredFields();

        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE ").append(tableName(obj)).append(" SET ");

        for (Field f : fields) {
            if (isTransient(f) || !isUpdatable(f)) {
                continue;
            }
            holder.addParam(convert(getValue(obj, f)));
            sql.append(columnName(f)).append("=?").append(",");

        }
        deleteLastComma(sql);
        sql.append(" WHERE ");
        sql.append(StringUtils.isNotBlank(where) ? where : " ");
        holder.setSql(sql.toString());
        logger.debug(holder);
        return holder;

    }

    /**
     * build update sql with query parameter
     * 
     * @param obj
     * @param where
     *            can not be empty
     * @return
     */
    public static SqlHolder buildUpdate(Object obj, Parameter param) {
        SqlHolder holder = new SqlHolder();
        Field[] fields = obj.getClass().getDeclaredFields();

        StringBuilder sql = new StringBuilder();
        StringBuilder wheres = new StringBuilder();

        sql.append("UPDATE ").append(tableName(obj)).append(" SET ");

        for (Field f : fields) {
            if (isTransient(f) || !isUpdatable(f)) {
                continue;
            }
            Object paramValue = convert(getValue(obj, f));

            // if the value of field is empty, continue
            if (paramValue == null) {
                continue;
            }
            holder.addParam(paramValue);
            sql.append(columnName(f)).append("=?").append(",");

        }
        deleteLastComma(sql);

        // iterate all parameters
        buildWheres(param, holder, wheres);

        sql.append(" WHERE ");
        sql.append(StringUtils.isNotBlank(wheres.toString()) ? wheres : " ");
        holder.setSql(sql.toString());
        logger.debug(holder);
        return holder;

    }

    /**
     * 构造delete
     * 
     * @param obj
     * @param where
     *            不允许为空
     * @return
     */
    public static SqlHolder buildDelete(Class<?> clazz, String where) {
        SqlHolder holder = new SqlHolder();
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM ").append(tableName(clazz));
        sql.append(" WHERE ");
        sql.append(StringUtils.isNotBlank(where) ? where : " ");
        holder.setSql(sql.toString());
        logger.debug(holder);
        return holder;

    }

    /**
     * 构造delete
     * 
     * @param obj
     * @param where
     *            不允许为空
     * @return
     */
    public static SqlHolder buildDelete(Class<?> clazz, Parameter param) {
        SqlHolder holder = new SqlHolder();
        StringBuilder sql = new StringBuilder();
        StringBuilder where = new StringBuilder();
        buildWheres(param, holder, where);
        sql.append("DELETE FROM ").append(tableName(clazz));
        //sql.append(" WHERE ");
        sql.append(StringUtils.isNotBlank(where.toString()) ? " WHERE " + where : " ");
        holder.setSql(sql.toString());
        logger.debug(holder);
        return holder;

    }

    private static void buildWheres(Parameter param, SqlHolder holder, StringBuilder wheres) {
        while (param != null) {
            wheres.append(param.getColumnName()).append("=?").append(" and ");
            holder.addParam(param.getValue());

            param = param.getNextParam();

        }
        deleteLast(wheres, " and ");
    }

    public static SqlHolder buildQuery(Class<?> clazz, Parameter param) {
        SqlHolder holder = new SqlHolder();
        Field[] fields = clazz.getDeclaredFields();

        StringBuilder columns = new StringBuilder();
        StringBuilder wheres = new StringBuilder();

        for (Field f : fields) {
            if (isTransient(f)) {
                continue;
            }
            columns.append(columnName(f)).append(",");
        }
        deleteLastComma(columns);

        // iterate all parameters
        buildWheres(param, holder, wheres);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ").append(columns).append(" FROM ").append(tableName(clazz));
        sql.append(wheres.length() == 0 ? " " : " WHERE " + wheres);
        holder.setSql(sql.toString());
        logger.debug(holder);
        return holder;

    }

    public static SqlHolder buildQuery(Class<?> clazz, String wheres) {
        SqlHolder holder = new SqlHolder();
        Field[] fields = clazz.getDeclaredFields();

        StringBuilder columns = new StringBuilder();

        for (Field f : fields) {
            if (isTransient(f)) {
                continue;
            }
            columns.append(columnName(f)).append(",");
        }
        deleteLastComma(columns);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ").append(columns).append(" FROM ").append(tableName(clazz));
        sql.append(wheres.length() == 0 ? " " : " WHERE " + wheres);
        holder.setSql(sql.toString());
        logger.debug(holder);
        return holder;

    }

    /**
     * 删除最后那个“,”
     * 
     * @param sb
     */
    private static void deleteLastComma(StringBuilder sb) {
        if (sb.lastIndexOf(",") == sb.length() - 1) {
            sb.deleteCharAt(sb.length() - 1);
        }
    }

    /**
     * 删除最后一个指定的单词
     * 
     * @param sb
     */
    private static void deleteLast(StringBuilder sb, String lastWord) {
        int lastIndex = sb.lastIndexOf(lastWord);
        if (lastIndex == sb.length() - lastWord.length()) {
            sb.delete(lastIndex, sb.length() - 1);
        }
    }

    /**
     * 获取列名<br/>
     * MyBeanProcessor中定义了查询时从数据库字段转 -> po属性 的规则,<br />
     * 此处po属性 -> 数据库字段 的规则和前面保持一致
     * 
     * @see MyBeanProcessor#prop2column(String)
     * @param f
     * @return
     */
    private static String columnName(Field f) {
        return MyBeanProcessor.prop2column(f.getName());
    }

    /**
     * get the value of objects.
     * 
     * @param obj
     * @param f
     * @return
     */
    private static Object getValue(Object obj, Field f) {
        Object o = null;
        try {
            if (f.isAccessible()) {
                o = f.get(obj);
            } else {
                f.setAccessible(true);
                o = f.get(obj);
                f.setAccessible(false);
            }
        } catch (Exception e) {
            logger.error("Failed to get value of objects.", e);
            throw new RuntimeException("Failed to get value of objects.", e);
        }
        return o;
    }

    /**
     * 
     * 属性是否可以修改
     * 
     * @param obj
     * @return
     */
    private static boolean isUpdatable(Field f) {
        Column c = f.getAnnotation(Column.class);
        if (c != null) {
            return c.updatable();
        }
        return true;
    }

    /**
     * 
     * 属性是否不需要持久化
     * 
     * @param obj
     * @return
     */
    private static boolean isTransient(Field f) {
        int m = f.getModifiers();
        // static 或 final的视为常量
        if (Modifier.isStatic(m) || Modifier.isFinal(m)) {
            return true;
        }
        Transient t = f.getAnnotation(Transient.class);
        if (t != null && t.value() == true) {
            return true;
        }
        return false;
    }

    /**
     * 
     * 获取表名
     * 
     * @param obj
     * @return
     */
    private static String tableName(Object obj) {
        String tableName = obj.getClass().getSimpleName();
        Table table = obj.getClass().getAnnotation(Table.class);
        if (table != null && StringUtils.isNotEmpty(table.name())) {
            tableName = table.name();
        }
        return tableName;
    }

    private static String tableName(Class<?> clazz) {
        String tableName = clazz.getSimpleName();
        Table table = clazz.getAnnotation(Table.class);
        if (table != null && StringUtils.isNotEmpty(table.name())) {
            tableName = table.name();
        }
        return tableName;
    }

    /**
     * 
     * convert java types 2 db types.
     * 
     * @param o
     * @return
     */
    private static Object convert(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof Date) {
            Date date = (Date) o;
            Timestamp t = new Timestamp(date.getTime());
            return t;
        }
        return o;
    }
}
