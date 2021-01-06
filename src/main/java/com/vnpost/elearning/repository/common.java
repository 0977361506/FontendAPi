package com.vnpost.elearning.repository;

import javax.persistence.Query;
import java.util.Map;

public class common {


    public Object[]  setParamValue(Map<String, Object> property){
        int i = 0;
        String[] params = new String[property.size()];
        Object[] values = new Object[property.size()];

        for (Map.Entry items : property.entrySet()) {
            params[i] = (String) items.getKey();
            values[i] = (Object) items.getValue();
            i++;
        }
        return new Object[] {params, values};
    }


    public void setPropertyLikeParamsSQL(StringBuilder sql,String[] params,Map<String, Object> property){
        if (property.size() > 0) {
            for (int i1 = 0; i1 < params.length; i1++) {
                sql.append(" and ").append(" " + params[i1]).append(" like CONCAT('%',:value" + i1 + ",'%')");
            }
        }
    }

    public void setPropertyParamsEqual(StringBuilder sql,String[] params,Map<String, Object> property){
        if (property.size() > 0) {
            for (int i1 = 0; i1 < params.length; i1++) {
                sql.append(" and ").append(" " + params[i1]).append(" =:value" + i1 + " ");
            }
        }
    }

    public void setPropertyValueSQL(Object[] values, Map<String, Object> property, String[] params, Query query){
        if (property.size() > 0) {
            for (int i2 = 0; i2 < params.length; i2++) {
                query.setParameter("value" + i2, values[i2]);
            }
        }
    }

    public void setOffsetLimit(Integer offset, Integer limit,Query query){
        if (offset != null && offset >= 0) {
            query.setFirstResult(offset);
        }
        if (limit != null && limit >= 0) {
            query.setMaxResults(limit);
        }
    }

    public void sort(StringBuilder sql, String sortExpression, String sortDirection){
        if (sortExpression!=null && sortDirection!=null) {
            sql.append(" order by ").append(sortExpression);
            sql.append(" "+(sortDirection.equals("1")?" asc ":" desc"));
        }
    }
}
