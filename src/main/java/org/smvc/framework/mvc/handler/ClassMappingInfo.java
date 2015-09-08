package org.smvc.framework.mvc.handler;

public class ClassMappingInfo {
    private Class<?> clazz;
    
    private MappingInfo mappingInfo;

    public ClassMappingInfo(Class<?> clazz, MappingInfo mappingInfo) {
        super();
        this.clazz = clazz;
        this.mappingInfo = mappingInfo;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public MappingInfo getMappingInfo() {
        return mappingInfo;
    }

    public void setMappingInfo(MappingInfo mappingInfo) {
        this.mappingInfo = mappingInfo;
    }
    
    
}
