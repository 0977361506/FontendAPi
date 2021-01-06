package com.vnpost.elearning.repository;



import com.vnpost.elearning.dto.AbstractsDTO;
import com.vnpost.elearning.repository.CommonAbstractDao;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("unchecked")
public class AbstractDao<ID extends Serializable, T>  extends CommonAbstractDao  {
    @PersistenceContext
    private EntityManager entityManager;

    private Class <T> persistenceClass;


    public AbstractDao() {
        this.persistenceClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }


    public String getSessionClassName() {
        return persistenceClass.getSimpleName();
    }



    public List<T> findByProperty(AbstractsDTO abstractsDTO) {
        List<T> list =new ArrayList<T>();
        try {
            StringBuffer sql = new StringBuffer("FROM ");
            sql.append(getSessionClassName()).append(" WHERE 1=1 ");
            setParam(sql, abstractsDTO);
            sort(sql, abstractsDTO);
            Query query = entityManager.createQuery(sql.toString());
            setValue(abstractsDTO, query);
            setOffsetLimit(abstractsDTO, query);
            list = query.getResultList();
            return list;

        }catch (Exception e){
            e.getMessage();

        }
        return null;
    }


    public Long countByProperty(AbstractsDTO abstractsDTO) {
        try {
            StringBuffer sql = new StringBuffer("SELECT COUNT(*) FROM ");
            sql.append(getSessionClassName()).append(" WHERE 1=1 ");
            setParam(sql,abstractsDTO);
            Query query =  entityManager.createQuery(sql.toString());
            setValue(abstractsDTO, query);
            return  Long.parseLong(query.getSingleResult().toString());
        }catch (Exception e){
            e.getMessage();

        }

        return null;
    }






}