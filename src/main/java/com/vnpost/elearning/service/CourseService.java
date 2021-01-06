package com.vnpost.elearning.service;

import com.vnpost.elearning.converter.CourseConverter;
import com.vnpost.elearning.dto.course.CourseDTO;
import com.vnpost.elearning.repository.CourseCustom;
import com.vnpost.elearning.security.MyUser;
import eln.common.core.entities.chapter.Chapter;
import eln.common.core.entities.course.Course;
import eln.common.core.entities.course.Coursecategory;
import eln.common.core.repository.course.CourseCategoryRepository;
import eln.common.core.repository.course.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CourseService extends CommonRepository<Course, CourseRepository> {

  @Autowired private CourseConverter converter;
  @Autowired private CourseRepository courseRepository;
  @Autowired private CourseCustom courseCustom;
  @Autowired private CourseCategoryRepository courseCategoryRepository;
  @Autowired EntityManager en;

  public CourseService(CourseRepository repo) {
    super(repo);
  }

  public List<Course> getAllCourseByIdCate(Long id){
    String hql = "from Course c  where c.coursecategory.id=:id";
    return  en.createQuery(hql).setParameter("id",id).setMaxResults(4).getResultList();
  }
  public int getNumberChapterCourseWare(List<Chapter> list) {

    int k = 0;
    int sltrue = 0;
    for (Chapter chapter : list) {
      k += chapter.getChapterCourseWares().size();
    }
    return k;
  }




//  public boolean checkIsChildId(Long idChild, Long idParentheck){
//    List<Long> listIdParent  = courseCategoryRepository.getAllParentIDById(idChild);
//    for (Iterator<Long> it = listIdParent.iterator(); it.hasNext();) {
//      Long s = it.next();
//      for(Long i : listIdParent){
//        if(i.toString().equals(idParentheck.toString())) return true;
//      }
//    }
//    return  false ;
//  }
//
//
//  public List<Long> getAllChildByParentId(List<Long> listsIDChild , List<Long> listParentId , List<Long> listIDCategory){
//    try {
//      if(listParentId.isEmpty()) {
//        return listsIDChild;
//      }
//      for(Long i : listParentId){
//        for(Long j : listIDCategory){
//          if(j!=i && checkIsChildId(j,i)) {
//            System.out.println("phần tử mới thêm vào là "+j);
//            listsIDChild.add(j);
//          }
//        }
//      }
//    }catch (Exception e){
//      return listsIDChild;
//    }
//    return getAllChildByParentId(listsIDChild,listsIDChild,listIDCategory);
//  }
//  // Test
//
//  public void inkq(Long k ){
//    List<Long> list2 = new ArrayList<>(200); // mảng kêt quả
//    List<Long> listCategory = courseCategoryRepository.findAllIdCourseCategory();
//    List<Long> listIdChild  = courseCategoryRepository.getAllIdByParentId(k);
//    List<Long> kq = getAllChildByParentId(list2,listIdChild,listCategory);
//    System.out.println("dm size là gi " +kq.size());
//    for(Long i : kq){
//      System.out.println("phần tử con là "+i);
//    }
//  }

  public long getToTalNumberPage(int size) {
    String hql = "from Course c where c.status=1";
    List<Course> list = en.createQuery(hql).getResultList();
    long pageCount = (long) Math.ceil(1.0 * (list.size() / size));
    return pageCount;
  }

  public List<Course> getCourseByPageNo(int start, int end) {
    String hql = "from Course c where c.status=1";
    List<Course> list =
        en.createQuery(hql).setFirstResult(start * end).setMaxResults(end).getResultList();
    return list;
  }

  public List<Course> findAlls() {

    String hql = "from Course c where c.status=1";

    List<Course> list = en.createQuery(hql).getResultList();
    return list;
  }

  public List<Course> findbyCategory(Long id, Long id2) {

    String hql = "from Course c where c.coursecategory.id=:cid and c.id!=:vid and c.status=1";
    Query query = en.createQuery(hql);
    query.setParameter("cid", id);
    query.setParameter("vid", id2);
    query.setMaxResults(3);
    List<Course> list = query.getResultList();
    return list;
  }


  public List<CourseDTO> findCourseNew() {
    return courseRepository.findCourseNew().stream()
        .map(item -> converter.convertToDTO(item))
        .collect(Collectors.toList());
  }



  public Object[] getListCourse(CourseDTO model) {
    Map<String, Object> map = new HashMap<String, Object>();
    Integer offset = setOffset(model);
    try {
      setValueMapHomepage(model, map);
      Object[] objects =
          courseCustom.findByPropertyLikeSQL(
              map, "created_date", "2", offset, model.getMaxPageItems());

      List<CourseDTO> list =
          ((List<Course>) objects[0])
              .stream().map(converter::convertToDTO).collect(Collectors.toList());
      objects[0] = list;
      return objects;

    } catch (Exception e) {
      System.out.println(e.getMessage());
      return null;
    }
  }

  private Integer setOffset(CourseDTO model) {
    if (model.getPage() != null && model.getPage() > 1) {
      return ((model.getPage() - 1) * model.getMaxPageItems());
    }
    return model.getFirstItem();
  }

  public Course findByChapterId(Long chapterId) {
    return repo.findByChapterId(chapterId);
  }

  private void setValueMapHomepage(CourseDTO model, Map<String, Object> map) {
    map.put("status", 1);
    if (model.getName() != null) {
      map.put("name", model.getName());
    }
    if (model.getCategoryId() != null) {
      setListCategoryCourse(map, model.getCategoryId());
    }
    if (model.getCode() != null) {
      map.put("code", model.getCode());
    }
  }

  private void setListCategoryCourse(Map<String, Object> map, Long categoryId) {
      List<Coursecategory> categoryLists = courseCategoryRepository.findAll();
      HashSet<Long> longHashSet = new HashSet<>();
      List<Long> idCategoryNeed = new ArrayList<>();
      List<Long> idTemporarys = new ArrayList<>();
      idTemporarys.add(categoryId);
      idCategoryNeed.add(categoryId);
      getListIdParent(categoryLists,longHashSet,idCategoryNeed,idTemporarys);
      setStringMap(map,idCategoryNeed);
  }

  private void setStringMap(Map<String, Object> map, List<Long> idCategoryNeed) {
      StringBuilder  result = new StringBuilder();
      result.append("( ");
      for (int i=0;i<idCategoryNeed.size();i++){
        if(i<idCategoryNeed.size()-1){
          result.append(idCategoryNeed.get(i).toString()).append(" , ");
        }else{
          result.append(idCategoryNeed.get(i).toString());
        }
      }
      result.append(" )");
      map.put("idParents",result.toString());
  }

  private Integer getListIdParent(List<Coursecategory> categoryLists, HashSet<Long> longHashSet, List<Long> idCategoryNeed, List<Long> idTemporarys) {
      List<Long>  idParents = idTemporarys;
      List<Long> idTemporary = new ArrayList<>();
      if(idParents.size()<=0){
        return 0;
      }
      for (Long id:idParents){
        for (Coursecategory category :categoryLists){

          if(category.getParentId()!=null){
            if((category.getParentId().toString()).equals(id.toString())){
              if(longHashSet.add(category.getId())){
                idCategoryNeed.add(category.getId());
                idTemporary.add(category.getId());
              }else{
                return  0;
              }
            }
          }
        }
      }
      idTemporarys = idTemporary;
      return  getListIdParent(categoryLists,longHashSet,idCategoryNeed,idTemporarys);
  }

  public Integer  getstepbystepByIdCorse(Long idCourse){
      return  courseRepository.getstepbystepByIdCorse(idCourse);
  }

  public Object[] getCourseFinish(CourseDTO courseDTO){
    MyUser user = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Integer offset = setOffset(courseDTO);
    Object[] objects =
            courseCustom.findCourseFinishByidUser(user.getId(),courseDTO.getName(),offset, courseDTO.getMaxPageItems());
    List<CourseDTO> list =
            ((List<Course>) objects[0])
                    .stream().map(converter::convertToDTO).collect(Collectors.toList());
    objects[0] = list;
    return objects;
  }

//  public List<Course> getListCoureForchecked(Long idu , Pageable pageable){
//    String hql = "select cou.* from course_join_request as c , course as cou where c.id_course= cou.id and c.id_user=:idu";
//    List<Course> courses = en.createNativeQuery(hql,Course.class).setParameter("idu",idu).getResultList();
//    return  courses;
//
//  }
//  public List<Course> getListCourseForCheckedBySearch(Long idu , String key , Pageable pageable){
//    String hql = "select cou.* from course_join_request as c , course as cou where c.id_course= cou.id and c.id_user=:idu and cou.name like :key" ;
//    List<Course> courses = en.createNativeQuery(hql,Course.class).setParameter("idu",idu).setParameter("key" ,key).getResultList();
//    return  courses;
//  }
  public Object[] myCourseCurent(CourseDTO courseDTO){
    MyUser user = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Integer offset = setOffset(courseDTO);
    Object[] objects =
            courseCustom.myCourseCurent(user.getId(),courseDTO.getName(),offset, courseDTO.getMaxPageItems());
    List<CourseDTO> list =
            ((List<Course>) objects[0])
                    .stream().map(converter::convertToDTO).collect(Collectors.toList());
    objects[0] = list;
    return objects;
  }



}
