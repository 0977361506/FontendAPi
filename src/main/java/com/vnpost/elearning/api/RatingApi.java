package com.vnpost.elearning.api;
//import com.vnpost.e_learning.bean.Stars;
//import com.vnpost.e_learning.dto.MyUser;
//import com.vnpost.e_learning.entities.Course;
//import com.vnpost.e_learning.entities.Rate;
//import com.vnpost.e_learning.services.ICourseJoinService;
//import com.vnpost.e_learning.services.ICourseService;
//import com.vnpost.e_learning.services.IRateService;
import com.vnpost.elearning.Beans.Stars;
import com.vnpost.elearning.dto.course.RateDTO;
import com.vnpost.elearning.dto.ServiceResult;
import com.vnpost.elearning.processor.CourseProcessor;
import com.vnpost.elearning.security.MyUser;
import com.vnpost.elearning.service.CourseJoinService;
import com.vnpost.elearning.service.CourseService;
import com.vnpost.elearning.service.RateService;
import com.vnpost.elearning.service.UserService;
import eln.common.core.entities.course.Course;
import eln.common.core.entities.course.Rate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class RatingApi {
    @Autowired
    RateService rateService ;
    @Autowired
    RateService rat;
    @Autowired
    CourseService c;
    @Autowired
    CourseJoinService cj;
    @Autowired
    UserService userService ;

    @Autowired
    private CourseProcessor courseProcessor;
    @GetMapping("/rating/star/{eID}/{m}")
    public  Object[]  getrating(@PathVariable("eID") int val , @PathVariable("m") long sl) {
        double avg = 0;
        List<Integer> songuyen1 = new ArrayList<Integer>();
        Course course = c.findById(sl).get();
        Rate rate = rat.saveRateByUser(course,val);
        List<Rate> slrate= course.getRates();
        // trung binh danh giá
        double  tong2 = tong(slrate);
        int sizeRate = slrate.size();
        DecimalFormat df = new DecimalFormat("#.##");
        String formatted = df.format(tong2/sizeRate);
        List<Rate> list3 = rat.getRateFromCourse(sl);
        for(Rate r: list3) {
            songuyen1.add(r.getValuess());
        }
        Object[] mangObjects = {sizeRate,songuyen1,formatted,rate.getId()};
        return mangObjects;
    }


    public double tong(List<Rate> list) {
        double m = 0.0 ;
        for(Rate rate : list) {
            m +=rate.getValuess();
        }
        return m;
    }

    // lấy các đánh giá
    @RequestMapping("/show/review")
    public boolean getStar(@RequestBody Stars stars) {
        if(stars!=null) {
            Rate rate = rat.findbyId(stars.getIdRate());
            if(rate!=null) {
                rate.setStar_one(stars.getStarOne());
                rate.setStar_two(stars.getStarTwo());
                rate.setStar_three(stars.getStarThree());
                rate.setStar_for(stars.getStarFor());
                rate.setStar_five(stars.getStarFive());
                rat.save(rate);
                return true;
            }
        }
        return false;
    }

    // check quyền xem user đăng kí khoa shocj này chưa

    @GetMapping("/check/role/user/{id}")
    public Object[] chekRole(@PathVariable("id") Long id) {
        MyUser user = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(userService.checkUserForRate(id)) {
            Rate rate = rateService.CheckUserRated(id,user.getId());
            if(rate!=null){
                Object[] objects = {1,rate.getValuess()};
                return objects; // neu tồn tại thì có thể bình luận kháo học
            }else{
                Object[] objects = {1,0};
                return objects; // neu tồn tại thì có thể bình luận kháo học
            }
        }
        return null;
    }

    @PostMapping("/course/rating")
    public ResponseEntity<ServiceResult> addRatingCourse(@RequestBody RateDTO rateDTO) {
        ServiceResult result = new ServiceResult();
        try {
            courseProcessor.addRatingToCourse(rateDTO);
            result.setMessage("success");
            result.setCode("200");
        }
        catch (DataIntegrityViolationException e) {
            result.setMessage("Bạn đã đánh giá khóa học");
            result.setCode("500");
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            e.printStackTrace();
            result.setMessage(e.getMessage());
            result.setCode("400");
        }
        return ResponseEntity.ok(result);
    }

}
