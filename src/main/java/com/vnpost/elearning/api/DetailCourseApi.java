package com.vnpost.elearning.api;

import com.vnpost.elearning.converter.ChapterConverter;
import com.vnpost.elearning.converter.CourseConverter;
import com.vnpost.elearning.converter.DocumentConverter;
import com.vnpost.elearning.converter.RateConverter;
import com.vnpost.elearning.dto.CommentDTO;
import com.vnpost.elearning.dto.course.CourseDTO;
import com.vnpost.elearning.security.MyUser;
import com.vnpost.elearning.service.*;
import eln.common.core.common.SystemConstant;
import eln.common.core.entities.course.Comment;
import eln.common.core.entities.course.Course;
import eln.common.core.entities.course.CourseJoin;
import eln.common.core.entities.course.Rate;
import eln.common.core.entities.document.Document;
import eln.common.core.repository.CommentRepository;
import eln.common.core.repository.course.CourseRepository;
import eln.common.core.repository.CourseWareRepository;
import eln.common.core.repository.CourseWareTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
public class DetailCourseApi {
    @Autowired
    RateConverter rateConverter;
    @Autowired
    CommentRepository commentRepository;
    @Autowired CourseService ccc;
    @Autowired
    ChapterConverter chapterConverter;
    @Autowired CommentService commentService;
    @Autowired
    CommentDTO commentDTO ;
    @Autowired
    DocumentConverter documentConverter;
    @Autowired
    CourseConverter converter;
    @Autowired
    CourseWareProcessService courseWarePrc;
    @Autowired
    private CourseWareTypeRepository courseWareTypeRepository;
    @Autowired
    private CourseWareRepository courseWareRepository;
    @Autowired
    FunctionCompetitonService func;
    @Autowired
    RateService r;
    @Autowired
    CommentService comment; // commnet
    @Autowired
    ChapterCourseWareService ChapterCourseWareservice;
    @Autowired
    CourseService c;
    @Autowired
    CourseJoinService cj;
    @Autowired
    UserService u;
    @Autowired
    CourseRepository cr;

    @PostMapping("/registration/course/{idc}/{idu}")

    public Boolean registration(@PathVariable("idc") Long idc, @PathVariable("idu") Long idu) {
       int k = cj.getListByCourseJionByUser(idu, idc);
        if (k == 0) {
           CourseJoin courseJoin = new CourseJoin();
            courseJoin.setUser(u.findById(idu).get());
            courseJoin.setCourse(c.findById(idc).get());
            courseJoin.setStatus(1);
            courseJoin.setJoin(1);
            cj.save(courseJoin);
            return  true;
        }

        return false;

    }

    @GetMapping("/danhgias/khoahoc/{id}") // tìm kiếm khóa hojccuar tôi theo tên gần dúng
    public ResponseEntity<Object[]> danhgiakhoahoc(@PathVariable("id") Long id) {
        long slpageComment = commentService.getToTalNumberPage(6, id);
        StringBuilder checkusserThamgia = new StringBuilder("");
        List<Course> list = cr.getAllByStatus(1);
        Course course = ccc.findById(id).get();
        List<Comment> comments = commentRepository.getListCommentFromCourse(id);
        List<Rate> rates = course.getRates(); // danh sach đnáh giá kháo học
        Rate rate = r.sortByid(id); // săp sêp đanh giá khoa học

        int sl = rates.size();
        Double tong = tong(rates);
        DecimalFormat df = new DecimalFormat("#.##");
        String formatted = new String();
        if (!tong.toString().equals(0)) {
            formatted = df.format(tong / sl);
        }
        else {
            formatted = "0";
        }
        MyUser user = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // List<Course> courseJoins = cj.getListByCourseJion(user.getId()); // khoa hocn ma usẻ tham gia
        try {
            if ( cj.existsByIdCourseAndIdUser(user.getId(),course.getId())) {
                checkusserThamgia.append("Bạn đã tham gia khóa học này");
            }
        } catch (Exception ex) {
            Course courseByUser = new Course();
        }
        Object[] objects = {
                converter.convertToDTO(course),
                rateConverter.convertToDTO(rate),
                formatted,
                sl,
                checkusserThamgia,
                commentDTO.convertToList(comments),
                slpageComment
        };
        return new ResponseEntity<Object[]>(objects, HttpStatus.OK);
    }


    @GetMapping("/home/coursess/detail/{id}")
    public ResponseEntity<Object[]> getAllMyCourseByKey(@PathVariable("id") Long id) {
        // List<Course> list = cr.getAllByStatus(1);
        List<Course> courseListLienquan = new ArrayList<>();
        Course course = cr.findByStatusId(1, id); // tim kiếm khoa học trong list khóa học
        List<Document> documents = course.getDocuments();
        List<Rate> rates = course.getRates(); // danh sach đnáh giá kháo học
        int sl = rates.size();
        Double tong = tong(rates);
        String formatted = new String();
        if (!tong.equals(0.0)) {
            DecimalFormat df = new DecimalFormat("#.##");
            formatted = df.format(tong / sl);
        }
        else {
            formatted = "0";
        }

        MyUser user = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // List<Course> courseJoins = cj.getListByCourseJion(user.getId()); // khoa hocn ma usẻ tham gia
        if (course.getCoursecategory() != null) {
            courseListLienquan = ccc.findbyCategory(course.getCoursecategory().getId(), course.getId());
        }
        CourseDTO courseDTO =  converter.convertToDTO(course);
        Integer a = cj.CheckRoleUseCourse(id, user.getId());
        Object[] objects = {
                0,
                formatted,
                null,
                converter.convertList(courseListLienquan),
                null,
                null,
                documentConverter.convertToList(documents),
                courseDTO,
                null,
                null,
                null,
                null,
                a,
                course.getCourseJoins().size(),
                checkTimeAll(courseDTO),
                cj.existsByIdCourseAndIdUser(user.getId(),course.getId())
        };
        // 0 : danh gia khoa học ; 1: tông đanh gia formates; 2 : sô sluowng đanh gia khoa hoc  sl
        // ; 3 : khoa hoc lien quan ;
        // 4: so luong chap ter :  0 : ; 5 : sô lương học học liệu hoàn thành thay bang 0  ; 6 ;
        // docments ; 7 chi tiet khoa học ;
        // 8: so luong hoc lieu ; 9 : comments ; 10 : danh sach chap ter : tam thoi thay ban 0; 11 :
        // khoa hoc lien quan
        return new ResponseEntity<Object[]>(objects, HttpStatus.OK);
    }
    public double tong(List<Rate> list) {
        double m = 0.0;
        for (Rate rate : list) {
            m += rate.getValuess();
        }
        return m;
    }
    public boolean checkCourseFinish(CourseDTO c){
        if(c.getCourseConfig().getRegisterStart()==null || c.getCourseConfig().getRegisterEnd()==null){
            return true ;
        }
        Date current = new Date();
        Date registerEnd = new Date(c.getCourseConfig().getRegisterEnd().getTime() + SystemConstant.ONE_DAY);
        Date registerStart = c.getCourseConfig().getRegisterStart();
        return (current.after(registerStart) && current.before(registerEnd));
    }


    public boolean checkStartAndEndFinnishLearning(CourseDTO courseDTO){
        Date  end = new Date(courseDTO.getCourseConfig().getEndLearning().getTime()+SystemConstant.ONE_DAY);
        Date  start = courseDTO.getCourseConfig().getStartLearning();
        if(start==null ||  end ==null){
            return true ;
        }
        Date current = new Date();

        return (current.after(start)  && current.before(end));
    }

    public int  checkTimeAll(CourseDTO courseDTO){
        boolean checktimeStartLearning = this.checkStartAndEndFinnishLearning(courseDTO);
        boolean checkCourseRegisterCourse = this.checkCourseFinish(courseDTO);
        if(!checkCourseRegisterCourse) {
            return 3; // quá thời gian đăng kí
        }else if(!checktimeStartLearning){
            return 2;
        }
        return 1;
    }
}
