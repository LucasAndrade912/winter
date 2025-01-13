package br.edu.ifpb.webFramework.utils;

import br.edu.ifpb.webFramework.persistence.Column;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class DataExtracted {
    private String className;
    private Boolean isEntity;
    private Field[] fields;
    private Integer totalFields;
    private String tableName;
    private List<Column> columns;
    private Integer totalColumns;

    public DataExtracted(String className, Boolean isEntity, Field[] fields, Integer totalFields, String tableName, List<Column> columns, Integer totalColumns) {
        this.className = className;
        this.isEntity = isEntity;
        this.fields = fields;
        this.totalFields = totalFields;
        this.tableName = tableName;
        this.columns = columns;
        this.totalColumns = totalColumns;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Boolean getEntity() {
        return isEntity;
    }

    public void setEntity(Boolean entity) {
        isEntity = entity;
    }

    public Field[] getFields() {
        return fields;
    }

    public void setFields(Field[] fields) {
        this.fields = fields;
    }

    public Integer getTotalFields() {
        return totalFields;
    }

    public void setTotalFields(Integer totalFields) {
        this.totalFields = totalFields;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public Integer getTotalColumns() {
        return totalColumns;
    }

    public void setTotalColumns(Integer totalColumns) {
        this.totalColumns = totalColumns;
    }

    @Override
    public String toString() {
        return "DataExtracted {" +
                "\n  className='" + className + '\'' +
                ",\n  isEntity=" + isEntity +
                ",\n  fields=" + Arrays.toString(fields) +
                ",\n  totalFields=" + totalFields +
                ",\n  tableName='" + tableName + '\'' +
                ",\n  columns=" + columns +
                ",\n  totalColumns=" + totalColumns +
                "\n}";
    }
}
