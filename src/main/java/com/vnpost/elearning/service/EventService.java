package com.vnpost.elearning.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.vnpost.elearning.converter.DetailCategoryEventConverter;
import com.vnpost.elearning.converter.EventConverter;
import com.vnpost.elearning.dto.EventDTO;

import eln.common.core.common.SystemConstant;
import eln.common.core.entities.document.DetailCategoryEvent;
import eln.common.core.entities.events.Eventt;
import eln.common.core.entities.events.QEventt;
import eln.common.core.entities.question.Question;
import eln.common.core.repository.blog.DetailCategoryEventRepository;
import eln.common.core.repository.blog.EventRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManagerFactory;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EventService extends CommonRepository<Eventt, EventRepository> {
  @Autowired EntityManagerFactory entityManagerFactory;
  @Autowired private EventConverter eventConverter;

  @Autowired private DetailCategoryEventRepository detailCategoryEventRepository;
  @Autowired private DetailCategoryEventConverter detailCategoryEventConverter;
  private final QEventt Q = QEventt.eventt;


  public EventService(EventRepository repo) {
    super(repo);
  }


  public List<Eventt> findByStatus(int status) {
    return repo.findByStatus(status);
  }

 



  public Object[] getListEvents(EventDTO eventDTO, Pageable pageable) {
    BooleanBuilder builder = commonBuilder(eventDTO);
    List<EventDTO> eventDTOS =  findAll(builder, pageable)
            .stream()
            .map(eventConverter::convertToDTO)
            .collect(Collectors.toList());
    Long count = count(eventDTO);

    return new Object[]{eventDTOS,count};
  }

  public Long count(EventDTO eventDTO) {
    BooleanBuilder builder = commonBuilder(eventDTO);
    return repo.count(builder);
  }

  private BooleanBuilder commonBuilder(EventDTO eventDTO) {
    BooleanBuilder builder = new BooleanBuilder();
    builder.and(Q.status.eq(SystemConstant.ENABLE));
    if (eventDTO == null) {
      return builder;
    }
    if (eventDTO.getIdCategory() != null) {
      builder.and(Q.detailCategoryEvent.id.in(getListCategory(eventDTO.getIdCategory())));
    }

    return builder;
  }



  private List<Long>  getListCategory(Long idCategory) {
      List<DetailCategoryEvent> categoryLists = detailCategoryEventRepository.findAll();
      HashSet<Long> longHashSet = new HashSet<>();
      List<Long> idCategoryNeed = new ArrayList<>();
      List<Long> idTemporarys = new ArrayList<>();
      idTemporarys.add(idCategory);
      idCategoryNeed.add(idCategory);
      getListIdParent(categoryLists,longHashSet,idCategoryNeed,idTemporarys);
      return idCategoryNeed;
  }



  private Integer getListIdParent(List<DetailCategoryEvent> categoryLists, HashSet<Long> longHashSet, List<Long> idCategoryNeed, List<Long> idTemporarys) {
    List<Long>  idParents = idTemporarys;
    List<Long> idTemporary = new ArrayList<>();
    if(idParents.size()<=0){
      return 0;
    }
    for (Long id:idParents){
      for (DetailCategoryEvent category :categoryLists){
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

  public List<EventDTO> getEnvetTopBest() {
    return repo.getEnvetTopBest().stream()
        .map(item -> eventConverter.convertToDTO(item))
        .collect(Collectors.toList());
  }

  public EventDTO findByIdAndStatus(Long id, Integer status) {
    return eventConverter.convertToDTO(repo.findByIdAndStatus(id, status));
  }

  public List<EventDTO> findAllByCategoryIdAndStatus(Long idCategory, Integer status) {
    return repo.findAllByDetailCategoryEventIdAndStatus(idCategory, status).stream()
        .map(eventConverter::convertToDTO)
        .collect(Collectors.toList());
  }

  public List<Eventt> findAll(BooleanBuilder predicate, Pageable pageable) {
    JPAQuery<Eventt> query = new JPAQuery<>(em);
    return query
            .from(Q)
            .where(predicate)
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .orderBy(Q.timeCreate.desc())
            .fetch();
  }
}
