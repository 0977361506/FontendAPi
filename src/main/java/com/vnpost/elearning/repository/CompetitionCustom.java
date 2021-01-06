package com.vnpost.elearning.repository;

import eln.common.core.entities.competition.Competition;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class CompetitionCustom extends  common {
    @PersistenceContext
    private EntityManager entityManager;


    public Object[] findByPropertyLikeSQL(Map<String, Object> property, String sortExpression, String sortDirection, Integer offset, Integer limit) {
        List<Competition> list = new ArrayList<Competition>();
        String idParents = "";
        for (Map.Entry items : property.entrySet()) {
            if(items.getKey().equals("idParents")){
                idParents =  items.getValue().toString();
                property.remove("idParents");
                break;
            }
        }

        String[] params = (String[]) setParamValue(property)[0];
        Object[] values =(Object[]) setParamValue(property)[1];
        try {
            StringBuilder sql = new StringBuilder("select  c.* from   competition  c with(nolock)   ");
            sql.append(" inner join competition_category cc on c.id_competition_category = cc.id ").
                append(" WHERE 1=1 ");
            setPropertyLikeParamsSQL(sql,params,property);
            if (idParents!="") {
                sql.append(" and ").append(" id_competition_category in ").append(idParents);
            }
            sql.append(" and cc.status = 0  ");
            sort(sql,sortExpression,sortDirection);
            Query query = entityManager.createNativeQuery(sql.toString(), Competition.class);
            setPropertyValueSQL(values,property,params,query);
            setOffsetLimit(offset,limit,query);


            StringBuilder sql2 = new StringBuilder("select  count(*) from  competition  c with(nolock)   ");
            sql2.append(" inner join competition_category cc on c.id_competition_category = cc.id ");
            sql2.append(" WHERE 1=1 ");;
            setPropertyLikeParamsSQL(sql2,params,property);
            if (idParents!="") {
                sql2.append(" and ").append(" id_competition_category in ").append(idParents);
            }
            sql2.append(" and cc.status = 0  ");

            Query query2 = entityManager.createNativeQuery(sql2.toString());
            setPropertyValueSQL(values,property,params,query2);
            Integer count = ((Integer) query2.getSingleResult()).intValue();
            list = query.getResultList();
            return new Object[]{list, count};
        }catch(Exception e){
            return null;
        }
    }


    public Object[] findByPropertyLikeSQLMyCompe(Map<String, Object> property,Long idUser
            , Integer offset, Integer limit,Integer  getCheckFinish) {
        List<Competition> list = new ArrayList<Competition>();
        String[] params = (String[]) setParamValue(property)[0];
        Object[] values =(Object[]) setParamValue(property)[1];
        try {
           StringBuilder sql = new StringBuilder("select  DISTINCT co.* from round_test r  inner join competition co  on r.id_competition = co.id  where 1=1   ");

            if (property.size() > 0) {
                for (int i1 = 0; i1 < params.length; i1++) {
                        sql.append(" and ").append(" " + params[i1]).append(" like CONCAT('%',:value" + i1 + ",'%')");
                }
            }

            if(getCheckFinish!=null){
                setSqlCheckFinish(sql,getCheckFinish,idUser);
            }else{
                sql.append(" and r.id in (select DISTINCT r.id from round_test r inner join candidates c on r.id = c.id_round_test where  c.id_user='"+idUser+"') ");
            }
            if (offset != null && offset >= 0) {
                sql.append(" ORDER BY  co.id  OFFSET "+ offset +" ROWS ");
            }
            if (limit != null && limit >= 0) {
                sql.append(" FETCH NEXT "+limit +" ROWS ONLY ");
            }
            Query query = entityManager.createNativeQuery(sql.toString(), Competition.class);
            setPropertyValueSQL(values,property,params,query);
            StringBuilder sql2 = new StringBuilder("select count(DISTINCT co.id) from round_test r  inner join competition co  on r.id_competition = co.id  where 1=1    ");
            if (property.size() > 0) {
                for (int i1 = 0; i1 < params.length; i1++) {
                        sql2.append(" and ").append(" " + params[i1]).append(" like CONCAT('%',:value" + i1 + ",'%')");
                }
            }
            if(getCheckFinish!=null){
                setSqlCheckFinish(sql2,getCheckFinish,idUser);
            }else{
                    sql2.append(" and r.id in (select DISTINCT r.id from round_test r inner join candidates c on r.id = c.id_round_test where  c.id_user='"+idUser+"') ");
            }
           // sql2.append(" and r.id in (select DISTINCT r.id from round_test r inner join candidates c on r.id = c.id_round_test where  c.id_user='"+idUser+"') ");
            Query query2 = entityManager.createNativeQuery(sql2.toString());
            setPropertyValueSQL(values,property,params,query2);
            Integer count = ((Integer) query2.getSingleResult()).intValue();
            list = query.getResultList();
            return new Object[]{list, count};
        }catch(Exception e){
            return null;
        }
    }

    private void setSqlCheckFinish(StringBuilder sql, Integer getCheckFinish,Long idUser) {
        if(getCheckFinish==1){
            sql.append(" and r.id in (select DISTINCT r.id from round_test r inner join candidates c on r.id = c.id_round_test where  c.id_user='"+idUser+"' and  c.counttest =0 and c.confirm =0) ");
        }else if(getCheckFinish==2){
            sql.append(" and r.id in (select DISTINCT r.id from round_test r inner join candidates c on r.id = c.id_round_test where  c.id_user='"+idUser+"' and c.counttest <> 0) ");
        }else{
            sql.append(" and r.id in (select DISTINCT r.id from round_test r inner join candidates c on r.id = c.id_round_test where  c.id_user='"+idUser+"' and c.confirm =1) ");
        }
    }


}
