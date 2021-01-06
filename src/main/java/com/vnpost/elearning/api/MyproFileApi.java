package com.vnpost.elearning.api;

import com.vnpost.elearning.Mailer.mailsend;
import com.vnpost.elearning.converter.UserConverter;
import com.vnpost.elearning.dto.ServiceResult;
import com.vnpost.elearning.dto.UsersDTO;
import com.vnpost.elearning.processor.UserProcessor;
import com.vnpost.elearning.security.MyUser;
import com.vnpost.elearning.service.UserService;
import eln.common.core.entities.user.User;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/profile")
@AllArgsConstructor
public class MyproFileApi {

    private UserConverter userConverter;
    private UserService userService;
    private mailsend mailsend;
    private PasswordEncoder passwordEncoder;
    private UserProcessor userProcessor;



    static String getNewPassword(int n)
    {
        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);
        List<Integer> arrIndex = new ArrayList<>();
        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());
            arrIndex.add(index);
            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }
        if(!isRightPattern(arrIndex,0,25)) {
            int index = (int) (24* Math.random());
            arrIndex.add(index);
            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }
        if(!isRightPattern(arrIndex,26,35)) {
            int index = (int) (8* Math.random()) + 26;
            arrIndex.add(index);
            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }
        if(!isRightPattern(arrIndex,36,60)) {
            int index = (int) (23* Math.random()) + 36;
            arrIndex.add(index);
            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }
        return sb.toString();
    }

    public static boolean isRightPattern(List<Integer> arr, final int from, final int to) {
        for (Integer integer : arr) {
            if (integer < to && integer > from) {
                return true;
            }
        }
        return false;
    }



    @RequestMapping("/update")
    public ResponseEntity<ServiceResult> updateMyProfile(@RequestBody UsersDTO user){
        MyUser myUsers = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user1 = userService.findById(myUsers.getId()).get();
        user1.setEmail(user.getEmail());
        user1.setPlace(user.getPlace());
        user1.setPhoneNumber(user.getPhoneNumber());
        user1.setBirthday(user.getBirthday());
//        user1.setUsername(user.getUsername()); không dược cập nhật username
        user1.setFullName(user.getFullName());
        user1.setGender(user.getGender());
        if (user.getBirthDateFomatted() != null) {
            try {
                user1.setBirthday(new SimpleDateFormat("dd/MM/yyyy").parse(user.getBirthDateFomatted()));
            } catch (ParseException e) {
                e.printStackTrace();
                return new ResponseEntity<>(new ServiceResult(e.getMessage(),"500"), HttpStatus.BAD_REQUEST);
            }
        }
        if(!StringUtils.isNotBlank(user.getImageUsers())){
            user1.setImageUsers("/static/images/user_techer.png");
        }
        else{
            user1.setImageUsers(user.getImageUsers());
        }
        userService.save(user1);
//        Object[] userProfile= {user1.getEmail(),user1.getPlace(),user1.getPhoneNumber(),
//                user1.getUsername(),user1.getFullName(),user1.getGender(),user1.getImageUsers()};
        return  ResponseEntity.ok(new ServiceResult("success","200")) ;
    }




    @GetMapping("/detail")
    public ResponseEntity<ServiceResult> myProfile() {
        MyUser myUsers = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userEntity = userService.findById(myUsers.getId()).get();
        UsersDTO dto = userConverter.convertToDTO(userEntity);
        ServiceResult result = new ServiceResult(dto,"Success","200");
        return ResponseEntity.ok(result);
    }
    @GetMapping("/full-name")
    public ResponseEntity<ServiceResult> myFullName() {
        MyUser myUsers = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userEntity = userService.findById(myUsers.getId()).get();
        ServiceResult result = new ServiceResult(userEntity.getFullName(),"Success","200");
        return ResponseEntity.ok(result);
    }
    @GetMapping("/userName")
    public ResponseEntity<ServiceResult> myUserName() {
        MyUser myUsers = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userEntity = userService.findById(myUsers.getId()).get();
        ServiceResult result = new ServiceResult(myUsers.getUsername(),"Success","200");
        return ResponseEntity.ok(result);
    }

    @PostMapping("/password")
    public ResponseEntity<ServiceResult> forgetPassword(@RequestBody UsersDTO usersDTO) {
        ServiceResult result = new ServiceResult();
        try {
            User user = userService.findByUserNameAndEmailAndStatus(usersDTO);
            String newPass = getNewPassword(6);
            String newPassEncoded = passwordEncoder.encode(newPass);
            userService.updatePassword(user.getId(),newPassEncoded);
            mailsend.send(usersDTO.getEmail(),"elearningVnpost@gmail.com","Quên mật khẩu","mật khẩu mới của bạn là : <strong>"+newPass+"</strong>");
            result.setMessage("success");
            result.setCode("200");
        } catch (MessagingException e) {
            e.printStackTrace();
            result.setMessage("failed");
            result.setCode("500");
        } catch (Exception e) {
            e.printStackTrace();
            result.setMessage(e.getMessage());
            result.setCode("500");
        }
        return ResponseEntity.ok(result);
    }

    @PutMapping("/password")
    public ResponseEntity<ServiceResult> updatePassword(@RequestBody UsersDTO usersDTO) {
        ServiceResult result = new ServiceResult();
        MyUser myUsers = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (usersDTO.getId().equals(null)) usersDTO.setId(myUsers.getId());
        try {
            userProcessor.updatePassword(usersDTO);
            result.setCode("200");
            result.setMessage("success");
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode("500");
            if (e.getMessage().equals("Mật khẩu cũ chưa chính xác")) result.setCode("400");
            result.setMessage(e.getMessage());
        }
        return ResponseEntity.ok(result);
    }
}
