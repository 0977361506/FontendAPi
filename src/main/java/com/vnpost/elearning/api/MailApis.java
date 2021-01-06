package com.vnpost.elearning.api;

import com.vnpost.elearning.Mailer.mailsend;
import com.vnpost.elearning.api.course.CourseRequestApi;
import com.vnpost.elearning.dto.MailConfigDTO;
import com.vnpost.elearning.dto.ServiceResult;
import com.vnpost.elearning.service.HelpDeskService;
import com.vnpost.elearning.service.MailConfigService;
import com.vnpost.elearning.service.MailContactService;
import eln.common.core.common.MailConstant;
import eln.common.core.entities.config.HelpDesk;
import eln.common.core.entities.user.MailContact;
import eln.common.core.entities.user.User;
import eln.common.core.repository.user.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import javax.mail.MessagingException;

@RestController
public class MailApis {
    @Autowired
    private MailContactService mail;
    @Autowired
    private HelpDeskService he;
    @Autowired
    private  UserRepository us;
    @Autowired
    private mailsend mal;
    @Autowired private MailConfigService mailConfigService;
    @Autowired private mailsend mailsend;
    private final Logger logger = LogManager.getLogger(CourseRequestApi.class);


    @RequestMapping("/api/getpass/email")
    public Boolean ChangePass(@RequestParam("val") String email) throws MessagingException  {
        if(email.equals("")) return false;
        else {
             User user =  us.findByEmail(email);

            if(user==null) {
                return false;
            }
            else {
               mal.send("lethehieu151098@gmail.com",user.getEmail(),"Láº¥y láº¡i máº­t kháº©u :","Máº­t kháº©u cá»§a báº¡n lÃ  :"+user.getPassword());
            //    mal.send(user.getEmail(),"lethehieu151098@gmail.com","Láº¥y láº¡i máº­t kháº©u :","Máº­t kháº©u cá»§a báº¡n lÃ  :"+user.getPassword());
                return true;
            }
        }
    }

    @RequestMapping("/api/registration")
    public int  save(@RequestBody HelpDesk helpDesk) {
        List<HelpDesk> mailContacts= he.findAll();
        if(check(helpDesk.getEmail(), mailContacts)== false) {
            if(helpDesk.getEmail().matches("^\\w+[A-Za-z0-9]*@\\w+[a-z0-9].com$")== true) {
                HelpDesk mailContact = new HelpDesk();
                mailContact.setEmail(helpDesk.getEmail());
                he.save(mailContact);
                MailConfigDTO mailConfigDTO = mailConfigService.findByCode(MailConstant.NEWSLETTERS);
                try {
                    mailsend.send(mailContact.getEmail(),"elearningVnpost@gmail.com", mailConfigDTO.getSubjects(),mailConfigDTO.getContent());
                } catch (MessagingException e) {
                    logger.error("error send mail"+e.getMessage());
                    e.printStackTrace();
                }

                return 1;
            }else return 2;
        }
        return 0;
    }
    
    @PostMapping("/api/home/support")
    public Integer SendMailContact(@RequestBody MailContact mailContact){
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        if(mailContact.getContents().equals("")||mailContact.getEmail().equals("")|| mailContact.getNamemail().equals("")){
            return  0 ;
        }
        else if(mailContact.getContents().length()<10){
            return 1;
        }
//        else if(!mailContact.getEmail().matches(EMAIL_PATTERN)){
//            return  2 ;
//        }
        else{
            mail.save(mailContact);
            return  3;
        }
    }

    @PostMapping("/api/support/create")
    public ResponseEntity<ServiceResult> createMailContact(@RequestBody MailContact mailContact){
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        if(mailContact.getContents().equals("")||mailContact.getEmail().equals("")|| mailContact.getNamemail().equals("")){
            return  new ResponseEntity<>(new ServiceResult("wrong email pattern","400"), HttpStatus.BAD_REQUEST) ;
        }
        if(mailContact.getContents().length()<10){
            return  new ResponseEntity<>(new ServiceResult("content is too short","400"), HttpStatus.BAD_REQUEST) ;
        }
        mail.save(mailContact);
        return  new ResponseEntity<>(new ServiceResult("success","200"), HttpStatus.OK) ;

    }


    public boolean check(String a, List<HelpDesk> list) {
        int i = 0;
        for (HelpDesk c : list) {
            if (a.equals(c.getEmail())) i++;
        }
        if (i > 0) return true;
        return false;
    }
}