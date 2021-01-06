package com.vnpost.elearning.processor;

import com.vnpost.elearning.Mailer.mailsend;
import com.vnpost.elearning.dto.*;
import com.vnpost.elearning.security.MyUser;
import com.vnpost.elearning.service.*;
import eln.common.core.common.CommonDateTime;
import eln.common.core.common.ConvertString;
import eln.common.core.common.MailConstant;
import eln.common.core.entities.course.Course;
import eln.common.core.entities.user.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.mail.MessagingException;
import java.util.List;

@Service
public class MailProcessor   {
  @Autowired private MailConfigService mailConfigService;
  @Autowired private UserService userService;
  @Autowired private CourseService courseService;



    public ServiceResult sendEmailFinishCourse(Long idRound,Long idCourse) {
        try {
            MyUser myUsers = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Course course = courseService.findById(idCourse).get();
            User user = userService.findById(myUsers.getId()).get();
            mailConfigService.sendEmailFinishCourse(user,course,idRound);
            return  new ServiceResult(null, "success", "200");
        }catch (Exception e){
            return new ServiceResult(null, "fail", "200");
        }
    }

    public ServiceResult sendEmailDeleteRegistionCourse(Long idCourse) {
            try {
                MyUser myUsers = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                Course course = courseService.findById(idCourse).get();
                User user = userService.findById(myUsers.getId()).get();
                mailConfigService.sendEmailDeleteRegistionCourse(user,course);
                return  new ServiceResult(null, "success", "200");
            } catch (Exception e) {
                return new ServiceResult(null, "fail", "200");
            }
    }

    public ServiceResult sendEmailRegistionCourseCheckByAdmin(Long idCourse) {
        try {
            MyUser myUsers = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Course course = courseService.findById(idCourse).get();
            User user = userService.findById(myUsers.getId()).get();
            mailConfigService.sendEmailRegistionCourseCheckByAdmin(user,course);
            return  new ServiceResult(null, "success", "200");
        } catch (Exception e) {
            return new ServiceResult(null, "fail", "200");
        }
    }

    public ServiceResult  sendEmailRegisterFree(Long idCourse) {
            try {
                MyUser myUsers = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                Course course = courseService.findById(idCourse).get();
                User user = userService.findById(myUsers.getId()).get();
                mailConfigService.sendEmailRegisterFree(user,course);
                return  new ServiceResult(null, "success", "200");
            } catch (Exception e) {
                return new ServiceResult(null, "fail", "200");
            }
    }







}
