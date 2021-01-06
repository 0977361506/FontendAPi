package com.vnpost.elearning.repository;

import eln.common.core.entities.course.Course;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CourseCustom extends common{
    @PersistenceContext
    private EntityManager entityManager;


    public Object[] findByPropertyLikeSQL(Map<String, Object> property, String sortExpression, String sortDirection, Integer offset, Integer limit) {
        List<Course> list = new ArrayList<Course>();
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
            StringBuilder sql = new StringBuilder("select   * from  ");
            sql.append(" course ").append(" with(nolock)  WHERE 1=1 ");
            setPropertyLikeParamsSQL(sql,params,property);
            if (idParents!="") {
                sql.append(" and ").append(" id_course_category in ").append(idParents);
            }
            sort(sql,sortExpression,sortDirection);
            Query query = entityManager.createNativeQuery(sql.toString(), Course.class);
            setPropertyValueSQL(values,property,params,query);
            setOffsetLimit(offset,limit,query);


            StringBuilder sql2 = new StringBuilder("select  count(*) from  ");
            sql2.append(" course ").append(" with(nolock)  WHERE 1=1 ");
            setPropertyLikeParamsSQL(sql2,params,property);
            if (idParents!="") {
                sql2.append(" and ").append(" id_course_category in ").append(idParents);
            }
            Query query2 = entityManager.createNativeQuery(sql2.toString());
            setPropertyValueSQL(values,property,params,query2);
            Integer count = ((Integer) query2.getSingleResult()).intValue();
            list = query.getResultList();
            return new Object[]{list, count};
        }catch(Exception e){
            return null;
        }
    }

    public Object[] findCourseFinishByidUser(Long idUser,String name,Integer offset, Integer limit) {
        List<Course> list = new ArrayList<Course>();
        try {
            StringBuilder sql = new StringBuilder("select cou.* from (select * from course_join where id_user =:idUser " +
                    "and id_course not in (select Distinct id_course from course_ware_process where id_user =:idUser )) as cj , course as cou where cj.id_course = cou.id ");
            if(StringUtils.isNotBlank(name)){
                sql.append(" and cou.name  like").append("  CONCAT('%',:name,'%')");
            }

            Query query = entityManager.createNativeQuery(sql.toString(), Course.class);
            query.setParameter("idUser",idUser);
            if(StringUtils.isNotBlank(name)) {
                query.setParameter("name", name);
            }
            setOffsetLimit(offset,limit,query);

            StringBuilder sql2 = new StringBuilder("select count(*) from (select * from course_join where id_user =:idUser " +
                    "and id_course not in (select Distinct id_course from course_ware_process where id_user =:idUser )) as cj , course as cou where cj.id_course = cou.id ");
            if(StringUtils.isNotBlank(name)){
                sql2.append("and cou.name like ").append("  CONCAT('%',:name,'%')");
            }
            Query query2 = entityManager.createNativeQuery(sql2.toString());
            query2.setParameter("idUser",idUser);
            if(StringUtils.isNotBlank(name)) {
                query2.setParameter("name", name);
            }
            Integer count = ((Integer) query2.getSingleResult()).intValue();
            list = query.getResultList();
            return new Object[]{list, count};
        }catch(Exception e){
            return null;
        }
    }

    public Object[] myCourseCurent(Long idUser,String name,Integer offset, Integer limit) {
        List<Course> list = new ArrayList<Course>();
        try {
            StringBuilder sql = new StringBuilder("select * from course where id in( select distinct(id_course) from course_join  where id_user =:idUser and is_join=1 )");
            if(StringUtils.isNotBlank(name)){
                sql.append(" and name like ").append("  CONCAT('%',:name,'%')");;
            }

            Query query = entityManager.createNativeQuery(sql.toString(), Course.class);
            query.setParameter("idUser",idUser);
            if(StringUtils.isNotBlank(name)) {
                query.setParameter("name", name);
            }
            setOffsetLimit(offset,limit,query);

            StringBuilder sql2 = new StringBuilder("select count(*) from course where id in( select distinct(id_course) from course_join  where  id_user =:idUser and is_join=1 )");
            if(StringUtils.isNotBlank(name)){
                sql2.append("and name like ").append("  CONCAT('%',:name,'%')");
            }
            Query query2 = entityManager.createNativeQuery(sql2.toString());
            query2.setParameter("idUser",idUser);
            if(StringUtils.isNotBlank(name)) {
                query2.setParameter("name", name);
            }
            Integer count = ((Integer) query2.getSingleResult()).intValue();
            list = query.getResultList();
            return new Object[]{list, count};
        }catch(Exception e){
            return null;
        }
    }





}
