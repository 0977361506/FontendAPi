package com.vnpost.elearning.api;


import com.vnpost.elearning.dto.ServiceResult;
import com.vnpost.elearning.processor.MailProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/send-email")
public class MailApi {
    @Autowired
    private MailProcessor mailProcessor;


    @GetMapping("/finish-course/{idCourse}/{idRound}")
    public ResponseEntity<ServiceResult> finshExamCourse(@PathVariable("idCourse") Long idCourse,
                                                         @PathVariable("idRound") Long idRound) {
        return  new ResponseEntity<>(mailProcessor.sendEmailFinishCourse(idRound,idCourse), HttpStatus.OK);
    }


    @GetMapping("/deleteRegistionCourse/{idCourse}")
    public ResponseEntity<ServiceResult> deleteRigistionCourse(@PathVariable("idCourse") Long idCourse) {
           return new ResponseEntity<>(mailProcessor.sendEmailDeleteRegistionCourse(idCourse), HttpStatus.OK);
    }

    @GetMapping("/RegistionCourseCheckByAdmin/{idCourse}")
    public ResponseEntity<ServiceResult> rigistionCourseCheckByAdmin(@PathVariable("idCourse") Long idCourse) {
        return new ResponseEntity<>(mailProcessor.sendEmailRegistionCourseCheckByAdmin(idCourse), HttpStatus.OK);
    }

    @GetMapping("/regitster-free/{idCourse}")
    public ResponseEntity<ServiceResult> sendEmailRegisterFree(@PathVariable("idCourse") Long idCourse) {
        return new ResponseEntity<>(mailProcessor.sendEmailRegisterFree(idCourse), HttpStatus.OK);
    }

}
