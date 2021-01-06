package com.vnpost.elearning.repository;

import eln.common.core.entities.question.Question;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class QuestionCustomRepository extends  common{
  @PersistenceContext private EntityManager entityManager;

    public Object[] findByPropertyRandom(Map<String, Object> property,Integer top) {
        List<Question> list = new ArrayList<Question>();
        String[] params = (String[]) setParamValue(property)[0];
        Object[] values =(Object[]) setParamValue(property)[1];
        try {
            StringBuilder sql = new StringBuilder("select ").append(" top ").append(top).append("  * from  ");
            sql.append(" question ").append(" with(nolock)  WHERE 1=1 ");
            setPropertyParamsEqual(sql,params,property);
            sql.append( " order by NEWID() ");
            Query query = entityManager.createNativeQuery(sql.toString(), Question.class);
            setPropertyValueSQL(values,property,params,query);
            list = query.getResultList();
            return new Object[] {list};
        } catch (Exception e) {
            return null;
        }
    }

  }
