package com.vnpost.elearning.dto.course;

import com.fasterxml.jackson.annotation.JsonIgnore;
import eln.common.core.entities.courseware.CourseWare;
import eln.common.core.entities.TypeMultimediaCourseWare;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.Map;

@Getter
@Setter
@Component
public class CourseWareDTO {
  private Long id;
  private Date createdDate;
  private Date modifiedDate;

  private String createdBy;
  private String modifiedBy;
  private Integer count;
  private String searchValue;
  @JsonIgnore private String[] listArray;

  private String urlMapping;
  private String message;
  private Integer checkValue;
  private String[] checkList;
  @JsonIgnore private Map<String, String> mapStrings;
  private String thumbnailBase64;
  private String[] listImage;

  private Long[] ids = new Long[] {};
  private Date timeCreate;
  private String typeCourseWareCode;
  private Long courseWareTypeId;
  private Long idCourse;
  private Long chapterId;
  private String content;
  private String unitCode;
  private Integer shared;
  private String name;
  private Integer id_unit;
  private Integer status;

  private MultipartFile[] fileDatas;
  private String files;
  private String description;
  private Date dateTimeEnd;
  private Integer type_video;
  private TypeMultimediaCourseWare type_multimedia2;
  private Date dateTimeStart;


  private CourseWareTypeDTO courseWareType;
 // @JsonIgnore private CourseWareType courseWareType2; // johnas
// @JsonIgnore private PoscodeVnpostDTO poscodeVnpost;
  private Integer id_poscode;
  private Integer statusComplete;
 // @JsonIgnore private List<CourseWareProcessDTO> courseWareProcesses = new ArrayList<>();

  //@JsonIgnore private List<ChapterCourseWareDTO> chapterCourseWares = new ArrayList<>();


  public CourseWareDTO convertToCourseWareDTO(CourseWare courseWare) {
    CourseWareDTO c = new CourseWareDTO();
    c.setId(courseWare.getId());
    c.setName(courseWare.getName());
    c.setCourseWareTypeId(courseWare.getCourseWareType().getId());
    c.setFiles(courseWare.getFiles());
    c.setContent(courseWare.getContent());
    c.setStatus(courseWare.getStatus());
    c.setType_video(courseWare.getType_video());
    c.setType_multimedia2(courseWare.getType_multimedia());
    return c;
  }

  private String file_download;
  private Long duration;

  private String idFileScorm;
  private Long totalQuitz;
  private Long percentFinished;
}
