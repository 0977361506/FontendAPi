package com.vnpost.elearning.dto;

import eln.common.core.entities.BaseEntity;
import eln.common.core.entities.course.Comment;
import eln.common.core.entities.course.Course;
import eln.common.core.entities.course.CourseConfig;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class ClassCouresDTO extends BaseEntity {

  private Date createdDate;
  private List<Comment> comments;

  public List<Comment> getComments() {
    return comments;
  }

  public void setComments(List<Comment> comments) {
    this.comments = comments;
  }

  private CourseConfig courseConfig;

  public CourseConfig getCourseConfig() {
    return courseConfig;
  }

  public void setCourseConfig(CourseConfig courseConfig) {
    this.courseConfig = courseConfig;
  }

  private Date modifiedDate;

  private String createdBy;

  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  public Date getModifiedDate() {
    return modifiedDate;
  }

  public void setModifiedDate(Date modifiedDate) {
    this.modifiedDate = modifiedDate;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public String getModifiedBy() {
    return modifiedBy;
  }

  public void setModifiedBy(String modifiedBy) {
    this.modifiedBy = modifiedBy;
  }

  private String modifiedBy;
  private Long id;
  private String avatar;

  private String description;

  private int highlight;

  private String name;

  private String code;
  private int price;

  private int showName;
  private int status; // trang thi hojc

  private int stepbystep; // lam tung buoec

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getAvatar() {
    return avatar;
  }

  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public int getHighlight() {
    return highlight;
  }

  public void setHighlight(int highlight) {
    this.highlight = highlight;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public int getPrice() {
    return price;
  }

  public void setPrice(int price) {
    this.price = price;
  }

  public int getShowName() {
    return showName;
  }

  public void setShowName(int showName) {
    this.showName = showName;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public int getStepbystep() {
    return stepbystep;
  }

  public void setStepbystep(int stepbystep) {
    this.stepbystep = stepbystep;
  }

  public ClassCouresDTO converttoEntityCourse(Course course) {
    ClassCouresDTO classCouresDTO = new ClassCouresDTO();
    classCouresDTO.setAvatar(course.getAvatar());
    classCouresDTO.setCode(course.getCode());
    classCouresDTO.setCreatedBy(course.getCreatedBy());
    classCouresDTO.setCreatedDate(course.getCreatedDate());
    classCouresDTO.setDescription(course.getDescription());
    classCouresDTO.setHighlight(course.getHighlight());
    classCouresDTO.setId(course.getId());
    classCouresDTO.setModifiedBy(course.getModifiedBy());
    classCouresDTO.setModifiedDate(course.getModifiedDate());
    classCouresDTO.setName(course.getName());
    classCouresDTO.setPrice(course.getPrice());
    //		classCouresDTO.setShowName(course.getShowName());
    classCouresDTO.setStatus(course.getStatus());
    //		classCouresDTO.setStepbystep(course.getStepbystep());
    classCouresDTO.setCourseConfig(course.getCourseConfig());
    classCouresDTO.setComments(course.getComments());

    return classCouresDTO;
  }
}
