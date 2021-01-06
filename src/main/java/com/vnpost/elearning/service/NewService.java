package com.vnpost.elearning.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.vnpost.elearning.converter.NewsConverter;
import com.vnpost.elearning.dto.EventDTO;
import com.vnpost.elearning.dto.NewsDTO;

import eln.common.core.common.SystemConstant;
import eln.common.core.entities.events.QEventt;
import eln.common.core.entities.news.New;
import eln.common.core.entities.news.NewCategory;
import eln.common.core.entities.news.QNew;
import eln.common.core.entities.question.Question;
import eln.common.core.repository.blog.NewCategoryRepository;
import eln.common.core.repository.blog.NewsRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class NewService extends CommonRepository<New, NewsRepository> {
  @Autowired NewsRepository res;

  @Autowired  private NewCategoryRepository newCategoryRepository;
  @Autowired  private NewsConverter newsConverter;
  @Autowired EntityManagerFactory entityManagerFactory;
  private final QNew Q = QNew.new$;


  public NewService(NewsRepository repo) {
    super(repo);
  }

  public List<New> findByIdCategory(Long categoryId) {
    return repo.findByNewCategoryId(categoryId);
  }


  public List<New> findByStatus(Integer status){
      return repo.findByStatusNew(status);
  }

  public List<New> phantrang(int size, int offSet) {
    Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
    String hString = "from New ";
    Query query = session.createQuery(hString);
    query.setFirstResult(size * offSet);
    query.setMaxResults(size);

    List<New> list = query.getResultList();
    return list;
  }

  public New findByIdAndStatus(Long id,Integer status){
      return repo.findByIdAndStatusNew(id,status);
  }


  public Object[] getListNew(NewsDTO newsDTO, Pageable pageable) {
    BooleanBuilder builder = commonBuilder(newsDTO);
    List<NewsDTO> eventDTOS =   findAll(builder, pageable).stream()
            .map(newsConverter::convertToDTO)
            .collect(Collectors.toList());
    Long count = count(newsDTO);

    return new Object[]{eventDTOS,count};
  }

  public List<New> findAll(BooleanBuilder predicate, Pageable pageable) {
    JPAQuery<New> query = new JPAQuery<>(em);
    return query
            .from(Q)
            .where(predicate)
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .orderBy(Q.timeCreate.desc())
            .fetch();
  }

  public Long count(NewsDTO newsDTO) {
    BooleanBuilder builder = commonBuilder(newsDTO);
    return repo.count(builder);
  }

  private BooleanBuilder commonBuilder(NewsDTO newsDTO) {
    BooleanBuilder builder = new BooleanBuilder();
    builder.and(Q.statusNew.eq(SystemConstant.ENABLE));
    if (newsDTO == null) {
      return builder;
    }
    if (newsDTO.getId_detail_category() != null) {
      builder.and(Q.newCategory.id.in(getListCategory(newsDTO.getId_detail_category())));
    }
    return builder;
  }




  private  List<Long> getListCategory(Long idDetailCategory) {
    List<NewCategory> categoryLists = newCategoryRepository.findAll();
    HashSet<Long> longHashSet = new HashSet<>();
    List<Long> idCategoryNeed = new ArrayList<>();
    List<Long> idTemporarys = new ArrayList<>();
    idTemporarys.add(idDetailCategory);
    idCategoryNeed.add(idDetailCategory);
    getListIdParent(categoryLists,longHashSet,idCategoryNeed,idTemporarys);
    return idCategoryNeed;
  }

  private Integer getListIdParent(List<NewCategory> categoryLists, HashSet<Long> longHashSet, List<Long> idCategoryNeed, List<Long> idTemporarys) {
    List<Long>  idParents = idTemporarys;
    List<Long> idTemporary = new ArrayList<>();
    if(idParents.size()<=0){
      return 0;
    }
    for (Long id:idParents){
      for (NewCategory category :categoryLists){

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


  public   List<NewsDTO> getListNewBest() {
    return res.findNewBest().stream()
            .map(item -> newsConverter.convertToDTO(item))
            .collect(Collectors.toList());
  }


}
