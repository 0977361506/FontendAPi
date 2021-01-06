package com.vnpost.elearning.repository;


import com.vnpost.elearning.dto.AbstractsDTO;

import javax.persistence.Query;
import java.util.List;
import java.util.Map;

public class CommonAbstractDao  extends  ManageFunctionDao{
    public void setParam(StringBuffer sql, AbstractsDTO abstractsDTO){
        setParamEqual(sql,abstractsDTO);
        setParamEqualOr(sql,abstractsDTO);
        setParamLike(sql,abstractsDTO);
        setParamIn(sql,abstractsDTO);
    }


    public void setParamEqualOr(StringBuffer sql, AbstractsDTO abstractsDTO) {
        if(abstractsDTO.getMapDTO().getValuesEqualOr()!=null){
            if(abstractsDTO.getMapDTO().getValuesEqualOr().size()>0){
                for (Map.Entry items : abstractsDTO.getMapDTO().getValuesEqualOr().entrySet()) {
                    sql.append(" AND ").append(setAndOr((String[]) items.getKey(), (Object[]) items.getValue()));
                }
            }
        }
    }

    private String setAndOr(String[] key, Object[] value) {
        StringBuilder  result = new StringBuilder();
        result.append("( ");
        result.append( key[0] ).append(" = ").append(value[0]).append( " OR ").append(key[1]).append(" = ")
                .append(value[1]).append(" )");
        return result.toString();
    }

    public void setParamEqual(StringBuffer sql, AbstractsDTO abstractsDTO) {
        if(abstractsDTO.getMapDTO().getValuesEqual()!=null){
            if(abstractsDTO.getMapDTO().getValuesEqual().size()>0){
                for (Map.Entry items : abstractsDTO.getMapDTO().getValuesEqual().entrySet()) {
                    sql.append(" AND ").append(items.getKey()).append(" =:value"+items.getKey().toString().replace(".","")+" ");
                }
            }
        }
    }

    public void setParamLike(StringBuffer sql, AbstractsDTO abstractsDTO) {
        if(abstractsDTO.getMapDTO().getValuesLike()!=null) {
            if (abstractsDTO.getMapDTO().getValuesLike().size() > 0) {
                for (Map.Entry items : abstractsDTO.getMapDTO().getValuesLike().entrySet()) {
                    sql.append(" AND ").append(items.getKey()).append(" LIKE '%' ||  :value" + items.getKey().toString().replace(".", "") + " ||  '%' ");
                }
            }
        }
    }

    private void setParamIn(StringBuffer sql, AbstractsDTO abstractsDTO) {
        if(abstractsDTO.getMapDTO().getValueIn()!=null) {
            if (abstractsDTO.getMapDTO().getValueIn().size() > 0) {
                for (Map.Entry items : abstractsDTO.getMapDTO().getValueIn().entrySet()) {
                    sql.append(" AND ").append(items.getKey()).append(" IN ").append(setList((List<Long>) items.getValue()));
                }
            }
        }
    }

    private String setList(List<Long> inList) {
        StringBuilder  result = new StringBuilder();
        result.append("( ");
        for (int i=0;i<inList.size();i++){
            if(i<inList.size()-1){
                result.append(inList.get(i).toString()).append(" , ");
            }else{
                result.append(inList.get(i).toString());
            }
        }
        result.append(" )");
        return result.toString();
    }

    public void setValue(AbstractsDTO abstractsDTO, Query query ){
        if(abstractsDTO.getMapDTO().getValuesEqual()!=null) {
            if (abstractsDTO.getMapDTO().getValuesEqual().size() > 0) {
                for (Map.Entry items : abstractsDTO.getMapDTO().getValuesEqual().entrySet()) {
                    query.setParameter("value" + items.getKey().toString().replace(".", ""), items.getValue());
                }
            }
        }
        if(abstractsDTO.getMapDTO().getValuesLike()!=null) {
            if (abstractsDTO.getMapDTO().getValuesLike().size() > 0) {
                for (Map.Entry items : abstractsDTO.getMapDTO().getValuesLike().entrySet()) {
                    query.setParameter("value" + items.getKey().toString().replace(".", ""), items.getValue());
                }
            }
        }

    }



    public void setOffsetLimit(AbstractsDTO abstractsDTO,Query query){
        if (abstractsDTO.getOffset() != null && abstractsDTO.getOffset()  >= 0) {
            query.setFirstResult(abstractsDTO.getOffset());
        }
        if (abstractsDTO.getLimit() != null && abstractsDTO.getLimit() >= 0) {
            query.setMaxResults(abstractsDTO.getLimit());
        }
    }

    public void sort(StringBuffer sql,AbstractsDTO abstractsDTO){
        if (abstractsDTO.getMapDTO().getSortKey()!=null && abstractsDTO.getMapDTO().getSortValue()!=null) {
            sql.append(" ORDER BY ").append(abstractsDTO.getMapDTO().getSortKey())
                    .append(" ").append(abstractsDTO.getMapDTO().getSortValue()).append(" ");
        }
    }
}
