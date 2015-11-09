package org.smvc.framework.orm.annotation;

/**
 * Query parameter
 * @author Big Martin
 *
 */
public class Parameter {
    private String columnName;
    private Object value;
    
    /**
     * link structure
     */
    private Parameter next = null;

    
    public Parameter(String columnName, Object value) {
        super();
        this.columnName = columnName;
        this.value = value;
    }

    /**
     * add next query node
     * @param columnName
     * @param value
     * @return
     */
    public Parameter appendParam(String columnName, Object value)
    {
        if (next == null)
        {
            next = new Parameter(columnName, value);
        }
        else{
            next.appendParam(columnName, value);
        }
        return this;
    }
    
    /**
     * get next query node
     * @param columnName
     * @param value
     * @return
     */
    public Parameter getNextParam()
    {
        return next;
    }
    
    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
    
}
