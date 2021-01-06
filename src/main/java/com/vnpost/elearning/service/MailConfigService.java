package com.vnpost.elearning.service;

import com.vnpost.elearning.converter.MailConfigConverter;
import com.vnpost.elearning.dto.MailConfigDTO;
import com.vnpost.elearning.dto.ServiceResult;
import com.vnpost.elearning.dto.UsingLogDTO;
import eln.common.core.common.CommonDateTime;
import eln.common.core.common.MailConstant;
import eln.common.core.entities.chapter.Chapter;
import eln.common.core.entities.competition.Candidate;
import eln.common.core.entities.course.Course;
import eln.common.core.entities.user.User;
import eln.common.core.repository.CandidateRepository;
import eln.common.core.repository.MailConfigRepository;
import eln.common.core.repository.course.ChapterRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MailConfigService {

  @Autowired private MailConfigRepository mailConfigRepository;
  @Autowired private MailConfigConverter mailConfigConverter;
  @Autowired private ChapterRepository chapterRepository;
  @Autowired private com.vnpost.elearning.Mailer.mailsend mailsend;
  @Autowired private CandidateRepository candidateRepository;
  @Autowired private MailConfigService mailConfigService;

  @Value("${uri.chatroom}")
  private String uriChatroom;

  public MailConfigDTO findByCode(String code) {
    return mailConfigConverter.convertToDTO(mailConfigRepository.findByCode(code));
  }




  public Integer getTotalDuration(String userName,Long idCourse){
    List<Long> idChapter = setIdChapter(chapterRepository.findByCourseId(idCourse));
    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uriChatroom+"/chatmess/api/usingLog/duration-total");
    HttpHeaders headers = new HttpHeaders();
    headers.set("Accept", "application/json");
    UsingLogDTO usingLogDTO = new UsingLogDTO();
    usingLogDTO.setUserName(userName);
    usingLogDTO.setIdChapters(idChapter);
    HttpEntity<UsingLogDTO> requestEntity = new HttpEntity<>(usingLogDTO, headers);
    RestTemplate restTemplate = new RestTemplate();
    HttpEntity<ServiceResult> response =
        restTemplate.exchange(
            builder.toUriString(), HttpMethod.POST, requestEntity, ServiceResult.class);
    return Integer.parseInt(response.getBody().getData().toString());
  }

  private List<Long> setIdChapter(List<Chapter> byCourseId) {
    List<Long> ids = new ArrayList<>();
    if(byCourseId.size()>0){
      for (Chapter chapter:byCourseId){
        ids.add(chapter.getId());
      }
    }
    return  ids;
  }

  public void sendEmailFinishCourse(User usersDTO, Course courseDTO, Long idRound) throws MessagingException {
    if (StringUtils.isNotBlank(usersDTO.getEmail())) {
      MailConfigDTO mailConfigDTO = mailConfigService.findByCode(MailConstant.FINISH_COURSE_EXAM);
      String content =
          mailConfigDTO
              .getContent()
              .replace(
                  MailConstant.ACCESS_TIME_COURSE,
                  (Math.floor(
                          (float) getTotalDuration(usersDTO.getUsername(), courseDTO.getId()) / 60))
                      + "")
              .replace(MailConstant.EXAM_RESULT, resultExamFinshCourse(idRound, usersDTO.getId()))
              .replace(MailConstant.NAME_COURSE, courseDTO.getName());

      mailsend.send(usersDTO.getEmail(), MailConstant.FROM, mailConfigDTO.getSubjects(), content);
    }
  }

  public void sendEmailCourseUnExam(User usersDTO, Course courseDTO) throws MessagingException {
    if (StringUtils.isNotBlank(usersDTO.getEmail())) {
      MailConfigDTO mailConfigDTO = mailConfigService.findByCode(MailConstant.FINISH_COURSE);
      String content =
          mailConfigDTO
              .getContent()
              .replace(
                  MailConstant.ACCESS_TIME_COURSE,
                  (Math.floor(
                          (float) getTotalDuration(usersDTO.getUsername(), courseDTO.getId()) / 60))
                      + "")
              .replace(MailConstant.NAME_COURSE, courseDTO.getName());

      mailsend.send(usersDTO.getEmail(), MailConstant.FROM, mailConfigDTO.getSubjects(), content);
    }
  }

  public void sendEmailDeleteRegistionCourse(User usersDTO, Course courseDTO)
      throws MessagingException {
    if (StringUtils.isNotBlank(usersDTO.getEmail())) {
      MailConfigDTO mailConfigDTO =
          mailConfigService.findByCode(MailConstant.DELETE_REGISTION_COURSE);
      String content =
          mailConfigDTO.getContent().replace(MailConstant.NAME_COURSE, courseDTO.getName());

      mailsend.send(usersDTO.getEmail(), MailConstant.FROM, mailConfigDTO.getSubjects(), content);
    }
  }

  public void sendEmailRegistionCourseCheckByAdmin(User usersDTO, Course courseDTO)
      throws MessagingException {
    if (StringUtils.isNotBlank(usersDTO.getEmail())) {
      MailConfigDTO mailConfigDTO =
          mailConfigService.findByCode(MailConstant.REGISTRATION_COURSE_BY_ADMIN);
      String content = setContentRegister(mailConfigDTO.getContent(), courseDTO);

      mailsend.send(usersDTO.getEmail(), MailConstant.FROM, mailConfigDTO.getSubjects(), content);
    }
  }

  public void sendEmailRegisterFree(User usersDTO, Course courseDTO) throws MessagingException {
    if (StringUtils.isNotBlank(usersDTO.getEmail())) {
      MailConfigDTO mailConfigDTO =
          mailConfigService.findByCode(MailConstant.FREE_REGISTRATION_COURSE);
      String content = setContentRegister(mailConfigDTO.getContent(), courseDTO);

      mailsend.send(usersDTO.getEmail(), MailConstant.FROM, mailConfigDTO.getSubjects(), content);
    }
  }

  private String setContentRegister(String content, Course courseDTO) {
    String contents = String.valueOf(content);
    contents = contents.replace(MailConstant.NAME_COURSE, courseDTO.getName());
    if (courseDTO.getCourseConfig().getEndLearning() != null) {
      contents = contents.replace(MailConstant.FROM_DATE, MailConstant.FROM_DATE_VI);
      contents = contents.replace(MailConstant.TO_DATE, MailConstant.TO_DATE_VI);
      contents =
          contents.replace(
              MailConstant.START_DATE,
              CommonDateTime.getDateTimeString(courseDTO.getCourseConfig().getStartLearning()));
      contents =
          contents.replace(
              MailConstant.END_DATE,
              CommonDateTime.getDateTimeString(courseDTO.getCourseConfig().getEndLearning()));
    } else {
      contents = contents.replace(MailConstant.FROM_DATE, "");
      contents = contents.replace(MailConstant.TO_DATE, "");
      contents = contents.replace(MailConstant.START_DATE, "");
      contents = contents.replace(MailConstant.END_DATE, MailConstant.NOT_SET_TIME);
    }
    if (courseDTO.getIsHasCertificate() == 0) {
      contents = contents.replace(MailConstant.CERTIFICATE, MailConstant.NOT_CERTIFICATE_VI);
    } else {
      contents = contents.replace(MailConstant.CERTIFICATE, MailConstant.RECOGNIZE_CERTIFICATE_VI);
    }
    return contents;
  }

  public String resultExamFinshCourse(Long idRound, Long idUser) {
    // List<CandidateDTO> candidateDTOS = candidateRepository.findByidUserIdRound(idUser, idRound);
    List<Candidate> candidateDTOS = candidateRepository.findByUserIdAndRoundTestId(idUser, idRound);
    Integer maxPoint = 0;
    Integer totalPoint = 0;
    if (candidateDTOS.size() > 0) {
      for (Candidate candidateDTO : candidateDTOS) {
        if (candidateDTO.getCounttest() > 0) {
          if (candidateDTO.getPoint() > maxPoint) {
            maxPoint = candidateDTO.getPoint();
            totalPoint = candidateDTO.getSumPoint();
          }
        }
      }
    }
    return maxPoint + "/" + totalPoint;
  }
}
