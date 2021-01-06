package com.vnpost.elearning.api.course;

import com.vnpost.elearning.Beans.CourseBean;
import com.vnpost.elearning.converter.*;
import com.vnpost.elearning.dto.*;
import com.vnpost.elearning.dto.competition.RoundTestDTO;
import com.vnpost.elearning.dto.course.ChapterDTO;
import com.vnpost.elearning.dto.course.CourseDTO;
import com.vnpost.elearning.dto.course.CourseWareDTO;
import com.vnpost.elearning.dto.course.CoursecategoryDTO;
import com.vnpost.elearning.dto.customDTO.CustomCourseProcessDTO;
import com.vnpost.elearning.dto.customDTO.DetailChapterCustomDTO;
import com.vnpost.elearning.dto.customDTO.DetailFinalCourseChapterDTO;
import com.vnpost.elearning.dto.customDTO.StatusExamDTO;
import eln.common.core.entities.chapter.Chapter;
import eln.common.core.entities.course.Comment;
import eln.common.core.entities.course.Course;
import eln.common.core.entities.course.Coursecategory;
import eln.common.core.entities.course.Rate;
import eln.common.core.entities.courseware.CourseWare;
import eln.common.core.entities.courseware.CourseWareProcess;
import eln.common.core.entities.document.Document;
import eln.common.core.exception.CourseException;
import eln.common.core.exception.CourseJoinException;
import com.vnpost.elearning.processor.ChapterCourseWareProcessor;
import com.vnpost.elearning.processor.CourseJoinProcessor;
import com.vnpost.elearning.processor.CourseProcessor;
import com.vnpost.elearning.security.MyUser;
import com.vnpost.elearning.service.*;
import eln.common.core.common.SystemConstant;
import eln.common.core.repository.*;
import eln.common.core.repository.course.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CourseApi {
    @Autowired
    private CourseJoinRequestService courseJoinRequestService;
    @Autowired
    private CourseWareConverter courseWareConverter;
    @Autowired
    private CandidateService can;
    @Autowired
    private RateConverter rateConverter;
    @Autowired
    private CourseWareRepository cres;
    @Autowired
    private ChapterConverter chapterConverter;
    @Autowired
    private  CommentDTO commentDTO;
    @Autowired
    private DocumentConverter documentConverter;
    @Autowired
    private CourseWareProcessService courseWareProcessService;
    @Autowired
    private CourseJoinService cj;
    @Autowired
    private ChapterCourseWareService ChapterCourseWareservice;
    @Autowired
    private RateService r;
    @Autowired
    private CourseService courseService;
    @Autowired
    private SlideShowService slide;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PoscodeVnpostRepository poscodeVnpostRepository;
    @Autowired
    private CourseJoinProcessor courseJoinProcessor;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CourseCategoryService d;
    @Autowired
    private CommentService commentService;
    @Autowired
    private CourseRepository cr;
    @Autowired
    private CourseConverter converter;
    @Autowired
    private CourseCategoryConverter categoryConverter;
    @Autowired
    CourseWareProcessRepository cwr;
    @Autowired
    private CustomCourseProcessConverter customCourseProcessConverter;
    @Autowired
    private RoundTestService roundTestService;
    @Autowired
    private CourseProcessor courseProcessor;
    @Autowired
    private CourseWareDTO dtos;
    @Autowired
    private ChapterCourseWareProcessor chapterCourseWareProcessor;
    @Autowired
    private CompetitionChapterService competitionChapterService;

    @GetMapping("/home/getTotalCourse")
    public long getTotalNumberCourse() {
        int number = 6;
        long size = courseService.getToTalNumberPage(number);
        return size;
    }

    @GetMapping("/home/getCourseByPageNo/{pageStart}")
    public Object[] getCourseByPageNo(@PathVariable("pageStart") int pageStart) {
        int number = 6;
        List<Course> courses = courseService.getCourseByPageNo(pageStart, number);
        List<Coursecategory> list2 = d.findAll();
        Object[] objects = {converter.convertList(courses), categoryConverter.convertList(list2)};
        return objects;
    }

    @GetMapping("/get/all/categoryCourse")
    public List<CoursecategoryDTO> getallCateCourse() {
        List<Coursecategory> list2 = d.findAll();
        return categoryConverter.convertList(list2);
    }

    @GetMapping("/get/allCourseByIdCate/{id}")
    public List<CourseDTO> getall(@PathVariable("id") Long id) {
        List<CourseDTO> courseDTOS = converter.convertList(courseService.getAllCourseByIdCate(id));
        return courseDTOS;
    }

    @PostMapping("/outline/detail")
    public ResponseEntity<?> getCoureWareById(@RequestBody CourseBean courseBean) {
        CourseWare courseWare = cres.findByFromId(courseBean.getIdcw());
        MyUser user = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (courseWare != null) {

            CourseWareDTO courseWareDTO = dtos.convertToCourseWareDTO(courseWare);
//            saveOrUpdateCourseWareProcess(user, courseBean, courseWare);
            int slCourseWare = ChapterCourseWareservice.getTotalListCourseWareFromChapterCourseWare(courseBean.getIdchapter());
            int courseWaresBystatus = ChapterCourseWareservice.getListCourseWareFromChapterCourseWareAndStatus(courseBean.getIdchapter(), user.getId(), 1);
            int checkhoanthanhcuocthicuoichuong = checkhoanthanhcuocthicuoichuong(user.getId(),courseBean.getIdchapter()); // không có bài thi cuối kì
            Object[] objects = {slCourseWare, courseWaresBystatus, courseWareDTO,checkhoanthanhcuocthicuoichuong}; // 0 : sô lương học lieuj hoàn thnafh  ; 1: tông so liêu coureware
            return new ResponseEntity<>(objects, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/check/percent/{idchapter}")
    public ResponseEntity<Object[]> checkPercentInChapter(@PathVariable("idchapter") Long idchapter){
        MyUser user = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int slCourseWare = ChapterCourseWareservice.getTotalListCourseWareFromChapterCourseWare(idchapter);
        int courseWaresBystatus = ChapterCourseWareservice.getListCourseWareFromChapterCourseWareAndStatus(idchapter, user.getId(), 1);
        int checkhoanthanhcuocthicuoichuong = checkhoanthanhcuocthicuoichuong(user.getId(),idchapter);
        Object[] objects = {slCourseWare, courseWaresBystatus,checkhoanthanhcuocthicuoichuong}; // 0 : sô lương học lieuj hoàn thnafh  ; 1: tông so liêu coureware
        return new ResponseEntity<>(objects, HttpStatus.OK);
    }
    @GetMapping("/chapter/process/{chapterId}")
    public ResponseEntity<ServiceResult> getProcessChapter(@PathVariable("chapterId") Long chapterId) {
        MyUser user = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer numberCourseWare = ChapterCourseWareservice.getTotalListCourseWareFromChapterCourseWare(chapterId);
        Integer numberCourseWarePass = ChapterCourseWareservice.getListCourseWareFromChapterCourseWareAndStatus(chapterId, user.getId(), 1);
        Integer checkhoanthanhcuocthicuoichuong = checkhoanthanhcuocthicuoichuong(user.getId(),chapterId); // không có bài thi cuối kì
        Integer process = 0;
        if (checkhoanthanhcuocthicuoichuong.equals(2)) {
            process = (numberCourseWarePass ) * 100 / (1 + numberCourseWare) ;
        }
        else {
            process = (numberCourseWarePass + checkhoanthanhcuocthicuoichuong) * 100 / (checkhoanthanhcuocthicuoichuong + numberCourseWare) ;

        }
        return ResponseEntity.ok(new ServiceResult(process,"success","200"));
    }

    @GetMapping("/home/index")
    public ResponseEntity<Object[]> getAllForIndex() {

        List<Course> co = cr.getAllByHighlightSQL(1);
        Object[] objects = {
                slide.findAllByCode("HOME"),
                co.stream().map(item -> converter.convertToDTO(item)).collect(Collectors.toList()),
                slide.findAllByCode("MOBILE")
        };
        return new ResponseEntity<Object[]>(objects, HttpStatus.OK);
    }


//    private void saveOrUpdateCourseWareProcess(MyUser user, CourseBean courseBean, CourseWare courseWare) {
//        List<CourseWareProcess> list =
//                cwr.getAllByIduAndIdChapterAndIdCourseWare(
//                        user.getId(), courseBean.getIdchapter(), courseBean.getIdcw());
//        if (list.size() == 0) {
//            CourseWareProcess courseWareProcess = new CourseWareProcess();
//            courseWareProcess.setChapter(chapterService.findById(courseBean.getIdchapter()).get());
//            courseWareProcess.setUser(users.findById(user.getId()).get());
//            courseWareProcess.setCourseWare(courseWare);
//            if (courseWare.getCourseWareType().getId() == 4||courseWare.getCourseWareType().getId() == 3) {
//                courseWareProcess.setStatus(1);
//            } else {
//                courseWareProcess.setStatus(SystemConstant.DISABLE);
//            }
//            courseWareProcess.setTotalView(1);
//            courseWareProcess.setLastView(1);
//            courseWareProcess.setDuration(0L);
//            courseWareProcess.setIdcourse(courseBean.getIdCourse());
//            courseWareProcessService.save(courseWareProcess);
//        } else if (list.size() == 1) {
////            list.get(0).setTotalView(list.get(0).getTotalView() + 1);
////            list.get(0).setLastView(list.get(0).getLastView() + 1);
////            courseWareProcessService.save(list.get(0));
//        }
//    }

    @RequestMapping("/home/course") // khoa hoc
    public ResponseEntity<Object[]> getAllForCourse(@RequestBody CourseDTO courseDTO) {

        Object[] objectsCourse = courseService.getListCourse(courseDTO);
        Object[] objects = {
                (List<CourseDTO>) objectsCourse[0],
                d.findParent(courseDTO),
                Integer.parseInt(objectsCourse[1].toString())
        };
        return new ResponseEntity<Object[]>(objects, HttpStatus.OK);
    }

    @PostMapping("/home/course/new") // khoa hoc
    public ResponseEntity<?> getCourseNews() {
        List<CourseDTO> courseDTOList = courseService.findCourseNew();
        return new ResponseEntity<>(courseDTOList, HttpStatus.OK);
    }




    public boolean checkCourseFinish(CourseDTO c) {
        if (c.getCourseConfig().getRegisterStart() == null || c.getCourseConfig().getRegisterEnd() == null) {
            return true;
        }
        Date current = new Date();
        Date registerEnd = new Date(c.getCourseConfig().getRegisterEnd().getTime() + SystemConstant.ONE_DAY);
        Date registerStart = c.getCourseConfig().getRegisterStart();
        return (current.after(registerStart) && current.before(registerEnd));
    }


    public boolean checkStartAndEndFinnishLearning(CourseDTO courseDTO) {
        if (courseDTO.getCourseConfig().getEndLearning() == null || courseDTO.getCourseConfig().getStartLearning() == null) {
            return true;
        }
        Date end = new Date(courseDTO.getCourseConfig().getEndLearning().getTime() + SystemConstant.ONE_DAY);
        Date start = courseDTO.getCourseConfig().getStartLearning();
        Date current = new Date();
        return (current.after(start) && current.before(end));
    }

    public int checkTimeAll(CourseDTO courseDTO) {
        boolean checktimeStartLearning = this.checkStartAndEndFinnishLearning(courseDTO);
        boolean checkCourseRegisterCourse = this.checkCourseFinish(courseDTO);
        if (!checkCourseRegisterCourse) {
            return 3; // quá thời gian đăng kí
        } else if (!checktimeStartLearning) {
            return 2;
        }
        return 1;
    }

    @GetMapping("/home/mycourses/{index}") // liệt kê khóa học của tôi
    public Object[] getAllMyCourse(@PathVariable("index") Integer index) {
        MyUser user = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Pageable pageable = PageRequest.of(index, 6);
        Page<Course> page = courseRepository.getListmyCourse(user.getId(), pageable);
        List<Course> courses = page.getContent();
        Integer totalCourse = courseRepository.getTotalMyCourse(user.getId());
        Object[] objects = {converter.convertList(courses), totalCourse};
        return objects;
    }

    @RequestMapping("/home/mycourses/{index}/{key}") // liệt kê khóa học của tôi
    public Object[] seacrhcMyCourse(@PathVariable("index") Integer index, @PathVariable("key") String key) {
        Pageable pageable = PageRequest.of(index, 6);
        MyUser user = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<Course> page = courseRepository.getAllMyCourseBySearch(user.getId(), "%" + key.trim() + "%", pageable);
        List<Course> courses = page.getContent();
        Integer sl = courseRepository.countSearchMyCourseInCourseJoin(user.getId(), "%" + key.trim() + "%");
        Object[] result = {converter.convertList(courses), sl};
        return result;
    }


    @GetMapping("/decuong/khoahoc/{id}")
    public ResponseEntity<?> decuongkhoahoc(@PathVariable("id") Long id) {
        Course course = courseService.findById(id).get();
        List<Chapter> chapters = course.getOutline().getChapters();//  chương mục của khóa học
        return new ResponseEntity<>(chapterConverter.convertToList(chapters), HttpStatus.OK);
    }

    @GetMapping("/danhgia/khoahoc/{id}") // tìm kiếm khóa hojccuar tôi theo tên gần dúng
    public ResponseEntity<Object[]> danhgiakhoahoc(@PathVariable("id") Long id) {
        Course course = courseService.findById(id).get();
        Rate rate = r.sortByid(id); // săp sêp đanh giá khoa học
        long sl = r.countRateFromCourse(id);
        MyUser user = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String formatted = trungbinhdanhgia(id);
        StringBuilder checkusserThamgia = checkusserThamgia(user.getId(), course.getId());
        Object[] objects = {
                converter.convertToDTO(course), rateConverter.convertToDTO(rate), formatted, sl, checkusserThamgia
        };
        return new ResponseEntity<Object[]>(objects, HttpStatus.OK);
    }


    @GetMapping("/binhluan/khoahoc/{id}")
    public ResponseEntity<Object[]> binhluankhoahoc(@PathVariable("id") Long id) {
        long slpageComment = commentService.getToTalNumberPage(6, id);
      //  Course course = courseService.findById(id).get();
      //  System.out.println("so luong comment là "+slpageComment);
        List<Comment> comments = commentRepository.getListCommentFromCourse(id);
      //  MyUser user = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Object[] objects = {
                commentDTO.convertToList(comments), slpageComment
        };
        return new ResponseEntity<Object[]>(objects, HttpStatus.OK);  // 2: sl: 3  -   7:courseDTO: 0  -  9: comments 5 -  0:rate:1
        // 1 : formated  : 2
    }

    @GetMapping("/home/courses/detail/{id}")
    public ResponseEntity<Object[]> getAllMyCourseByKey(@PathVariable("id") Long id) {
        List<Course> courseListLienquan = new ArrayList<>();
        Course course = cr.findById(id).get(); // tim kiếm khoa học trong list khóa học
        List<Document> documents = course.getDocuments();
        String formatted = trungbinhdanhgia(id);
        MyUser user = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (course.getCoursecategory() != null) {
            courseListLienquan = courseService.findbyCategory(course.getCoursecategory().getId(), course.getId());
        }
        Boolean checkExitsInRequest =
                courseJoinRequestService.exitsUserAndCourseId(user.getId(),id);
        CourseDTO courseDTO = converter.convertToDTO(course);
        Integer a = cj.CheckRoleUseCourse(id, user.getId());
        Object[] objects = {
                0, formatted, checkExitsInRequest, converter.convertList(courseListLienquan), null, null,
                documentConverter.convertToList(documents), courseDTO, null, null, null, null, a, course.getCourseJoins().size(), checkTimeAll(courseDTO),
                cj.existsByIdCourseAndIdUser(user.getId(), course.getId())
        };
        return new ResponseEntity<Object[]>(objects, HttpStatus.OK);
    }


    public StringBuilder checkusserThamgia(Long idu, long idc) {
        StringBuilder checkusserThamgia = new StringBuilder("");
        try {
            if (cj.existsByIdCourseAndIdUser(idu, idc)) {
                checkusserThamgia.append("Bạn đã tham gia khóa học này");
            }
        } catch (Exception ex) {
            Course courseByUser = new Course();
        }
        return checkusserThamgia;
    }


    public double tong(List<Rate> list) { // tông danh gia
        double m = 0.0;
        for (Rate rate : list) {
            m += rate.getValuess();
        }
        return m;
    }

    public String trungbinhdanhgia(Long id) {
        long sl = r.countRateFromCourse(id);
        Double tong = Double.valueOf(r.sumRate(id));
        String formatted = new String();
        if (!tong.equals(0.0)) {
            DecimalFormat df = new DecimalFormat("#.##");
            formatted = df.format(tong / sl);
        } else {
            formatted = "0";
        }
        return formatted;
    }

    @GetMapping("/outline/final/{a}") // lấy ra các học liêu thuộc một chương
    public ResponseEntity<?> getOutlineForCuorse(@PathVariable("a") Long id)
            throws CourseJoinException {
        MyUser myUsers = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CourseDTO courseDTO = courseProcessor.findByChapterId(id);
        if (!courseJoinProcessor.exitsUserAndCourse(myUsers.getId(), courseDTO.getId())) {
            throw new CourseJoinException("Chưa đăng nhập");
        }
        List<CourseWare> courseWares =
                ChapterCourseWareservice.getListCourseWareFromChapterCourseWare(id);
        List<CustomCourseProcessDTO> courseWareProcessList = new ArrayList<>();
        for (CourseWare c : courseWares) {
            if (cwr.getAllByIduAndIdChapterAndIdCourseWare(myUsers.getId(), id, c.getId()).size() > 0) {
                CourseWareProcess courseWareProcess =
                        cwr.getAllByIduAndIdChapterAndIdCourseWare(myUsers.getId(), id, c.getId()).get(0);
                CustomCourseProcessDTO courseProcessDTO =
                        customCourseProcessConverter.converter(courseWareProcess);
                courseWareProcessList.add(courseProcessDTO);
            } else {
                CustomCourseProcessDTO courseProcessDTO =
                        customCourseProcessConverter.converter(c);
                courseWareProcessList.add(courseProcessDTO);
            }
        }
        if (courseWareProcessList.size() > 0) {
            Long  idRound = competitionChapterService.findidRoundByidChapter(id);
            return new ResponseEntity<>(
                    new ServiceResult(new DetailFinalCourseChapterDTO(courseWareProcessList, competitionChapterService.setStatusExamDTO(idRound)), "Thành công", "200"), HttpStatus.OK);
        }

        return new ResponseEntity<>(
                new ServiceResult("Không có học liệu", "404"), HttpStatus.NOT_FOUND);
    }

    @GetMapping("/outline/out/{a}") // lấy ra các học liêu thuộc một chương out
    public ResponseEntity<List<CourseWareDTO>> getOutlineForCuorseOut(@PathVariable("a") Long id)
            throws CourseJoinException {
      //  MyUser myUsers = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       // CourseDTO courseDTO = courseProcessor.findByChapterId(id);
        List<CourseWare> courseWares =
                ChapterCourseWareservice.getListCourseWareFromChapterCourseWare(id);
        return new ResponseEntity<>(
                courseWareConverter.convertToList(courseWares), HttpStatus.OK);
    }

    @RequestMapping("/tile/{id}")
    public Object[] gettile(@PathVariable("id") Long id) {

        MyUser user = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int slCourseWare = ChapterCourseWareservice.getTotalListCourseWareFromChapterCourseWare(id);
        if(slCourseWare==0) return new Object[] {1,0,0};
        int courseWaresBystatus =  ChapterCourseWareservice.getListCourseWareFromChapterCourseWareAndStatus(id, user.getId(), 1);
        int checkhoanthanhcuocthicuoichuong = 0; // không có bài thi cuối kì
        if(competitionChapterService.findidRoundByidChapter(id)!=null){
            long idround = competitionChapterService.findidRoundByidChapter(id);

            if (can.checkCompetitonChapterComplete(user.getId(), idround) > 0) {
                checkhoanthanhcuocthicuoichuong = 1; // có bài thi va da hoan thanh
            }else  checkhoanthanhcuocthicuoichuong=2;
        }
        Object[] objects = {slCourseWare, courseWaresBystatus,checkhoanthanhcuocthicuoichuong};

        return objects;
    }

    @GetMapping("/outline/{a}/{index}/{chapterBefor}") // lấy ra các học liêu thuộc một chương in
    public ResponseEntity<?> getOutlineForCuorseIn(
            @PathVariable("a") Long id,
            @PathVariable("index") Integer index,
            @PathVariable("chapterBefor") Long chapterBefor)
            throws CourseJoinException {
        MyUser myUsers = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        CourseDTO courseDTO = courseProcessor.findByChapterId(id);

        if (checkOpen(myUsers, index, chapterBefor, courseDTO) == 1) {
            return new ResponseEntity<>("1", HttpStatus.OK);
        }

        if (!courseJoinProcessor.exitsUserAndCourse(myUsers.getId(), courseDTO.getId())) {
            throw new CourseJoinException("Chưa đăng nhập");
        }
        int slCourseWare = ChapterCourseWareservice.getTotalListCourseWareFromChapterCourseWare(id);
        int courseWaresBystatus = ChapterCourseWareservice.getListCourseWareFromChapterCourseWareAndStatus(id, myUsers.getId(), 1);
        int checkhoanthanhcuocthicuoichuong = checkhoanthanhcuocthicuoichuong(myUsers.getId(),id);
        int checkphantram = tinhphantramchuongmuc(checkhoanthanhcuocthicuoichuong,slCourseWare,courseWaresBystatus);
        List<CourseWareDTO> courseWares =
                chapterCourseWareProcessor.getListCourseWareFromChapterCourseWareSQL(id);

        return new ResponseEntity<>(new DetailChapterCustomDTO(courseWares,
                competitionChapterService.findidRoundByidChapter(id),checkphantram), HttpStatus.OK);
    }


    private Integer checkOpen(MyUser myUsers, Integer index, Long chapterBefor, CourseDTO courseDTO) {
        // không có bài thi cuối kì

        if (index != 0) {
            Integer size = ChapterCourseWareservice.getTotalListCourseWareFromChapterCourseWare(chapterBefor);
            Integer count =  ChapterCourseWareservice.getListCourseWareFromChapterCourseWareAndStatus(chapterBefor, myUsers.getId(), 1);
            int checkhoanthanhcuocthicuoichuong = checkhoanthanhcuocthicuoichuong(myUsers.getId(),chapterBefor);
            int checkphantram = tinhphantramchuongmuc(checkhoanthanhcuocthicuoichuong,size,count);
           // Chapter chapter = chapterRepository.findById(chapterBefor).get();
            if (courseDTO.getStepbystep() != null) {
                List<CourseWareProcess> courseWareProcess =
                        cwr.findByIduIdChapterId(myUsers.getId(), chapterBefor);
                if (courseDTO.getStepbystep() == 1 && courseWareProcess.size() > 0) {

                    if (checkphantram>=100) {
                        return 0;
                    } else {
                        return 1;
                    }
                } else {
                    return 1;
                }
            }
        }
        return 0;
    }


    public boolean checkCompleteChapter(Long idchapter,Long idu){
        Integer size = ChapterCourseWareservice.getTotalListCourseWareFromChapterCourseWare(idchapter);
        Integer count =  ChapterCourseWareservice.getListCourseWareFromChapterCourseWareAndStatus(idchapter, idu, 1);
        int checkhoanthanhcuocthicuoichuong = checkhoanthanhcuocthicuoichuong(idu,idchapter);
        int checkphantram = tinhphantramchuongmuc(checkhoanthanhcuocthicuoichuong,size,count);
        if(checkphantram>=100) return true ;
        return false;
    }
    public  List<ChapterDTO> updateChapterComplete(List<ChapterDTO> chapters, Long idu){
        for(ChapterDTO c : chapters){
            if(checkCompleteChapter(c.getId(),idu)) c.setCheckCompleteChapter(true);
            else c.setCheckCompleteChapter(false);
        }
        return chapters;
    }
    public int checkhoanthanhcuocthicuoichuong(Long idu , Long idchapter) {
        int checkhoanthanhcuocthicuoichuong = 0; // không có bài thi cuối kì

        if (competitionChapterService.findidRoundByidChapter(idchapter) != null) {
            long idround = competitionChapterService.findidRoundByidChapter(idchapter);
            if (can.checkCompetitonChapterComplete(idu, idround) > 0) {
                checkhoanthanhcuocthicuoichuong = 1; // có bài thi va da hoan thanh
            }else return checkhoanthanhcuocthicuoichuong=2;
        }
        return checkhoanthanhcuocthicuoichuong;
    }

    public int tinhphantramchuongmuc(Integer checkhoanthanhcuocthicuoichuong,Integer slcourseWare , Integer slhoanthanh){
        float phantrams = 0;
        if(checkhoanthanhcuocthicuoichuong==0){

            phantrams = (((float)slhoanthanh / slcourseWare) * 100);
            if(phantrams>100){
                phantrams=100;
            }
        }
        else if(checkhoanthanhcuocthicuoichuong==2){

            phantrams = (((float)slhoanthanh/ (slcourseWare+1)) * 100);
            if(phantrams>100){
                phantrams=100;

            }
        }
        else if(checkhoanthanhcuocthicuoichuong==1){

            phantrams = (((float)(slhoanthanh+checkhoanthanhcuocthicuoichuong) / (slcourseWare+1)) * 100);
            if(phantrams>100){
                phantrams=100;
            }
        }
        int checkphantram = (int) Math.floor(phantrams);
        return  checkphantram;
    }


    @GetMapping("/course/all")
    public ResponseEntity<ServiceResult> allCourse() {
        ServiceResult serviceResult = new ServiceResult();
        serviceResult.setData(courseProcessor.findAll());
        serviceResult.setCode("200");
        serviceResult.setMessage("success");
        return ResponseEntity.ok(serviceResult);
    }

    @GetMapping("/course/{id}")
    public ResponseEntity<ServiceResult> findById(@PathVariable Long id) {
        ServiceResult serviceResult = new ServiceResult("Khóa học", "200");
        try {
            serviceResult.setData(courseProcessor.findById(id));
        } catch (CourseException e) {
            serviceResult.setMessage(e.getMessage());
            serviceResult.setCode("500");
            return new ResponseEntity<>(serviceResult, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(serviceResult);
    }



    @GetMapping("/append/learn/course/{id}")
    public ResponseEntity<Object[]> goLearning(@PathVariable("id") Long id)
            throws CourseJoinException {
        MyUser user = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!courseJoinProcessor.exitsUserAndCourse(user.getId(), id)) {
            throw new CourseJoinException("Không có trong khóa học");
        }
        Course course = courseService.findById(id).get();
        List<Document> documents = course.getDocuments();
        List<Chapter> chapters = course.getOutline().getChapters(); //  chương mục của khóa học
        StringBuilder checkusserThamgia = checkusserThamgia(user.getId(), course.getId());
        List<Long> listIdRound = getListIdRound(course);
        StatusExamDTO statusExamDTO =new StatusExamDTO();
        if(listIdRound.size()>0){
            statusExamDTO =  competitionChapterService.setStatusExamDTO(listIdRound.get(0));
        }
        CourseDTO courseDTO = converter.convertToDTO(course);
        List<ChapterDTO> chapterDTOS = updateChapterComplete(chapterConverter.convertToList(chapters),user.getId());
        Object[] objects = {
                null, null, null, null, chapters.size(), null, documentConverter.convertToList(documents), null,
                null, null, chapterDTOS, null, checkusserThamgia, null,
                null, null, null, null, null, null, id, user.getId(), checkLearningStartEnd(courseDTO),statusExamDTO,
                null //24   2: sl  7:courseDTO  9: comments  0:rate
                // res[5 : sum ; res8 : soluong . res7 : courseDTO ,  res10 :chapterDTOS res23 :   statusExamDTO
        };
        return new ResponseEntity<Object[]>(objects, HttpStatus.OK);
    }


    public List<Long> getListIdRound(Course course) {
        List<Long> listIdRound = new ArrayList<>();
        if (course.getCompetitionId() != null) {
            List<RoundTestDTO> roundTestDTOList = roundTestService.findByIdCompetition(course.getCompetitionId());
            if (roundTestDTOList.size() > 0) {
                for (RoundTestDTO r : roundTestDTOList) {
                    listIdRound.add(r.getId());
                }
            }
        }
        return listIdRound;
    }

    public int sohocllieuhoanthanh(List<Chapter> chapters, MyUser user) {
        int sum = 0;
        for (Chapter c : chapters) {
            for (CourseWare courseWare :
                    ChapterCourseWareservice.getListCourseWareFromChapterCourseWare(c.getId())) {
                sum +=
                        courseWareProcessService
                                .countAllCourseFromCourseWareProcess(user.getId(), courseWare.getId(), 1, c.getId());
            }
        }
        return sum;

    }


    public boolean checkLearningStartEnd(CourseDTO courseDTO) {
        if (courseDTO.getCourseConfig().getStartLearning() == null || courseDTO.getCourseConfig().getEndLearning() == null) {
            return true;
        }
        Date current = new Date();
        Date end = new Date(courseDTO.getCourseConfig().getEndLearning().getTime() + SystemConstant.ONE_DAY);
        Date start = courseDTO.getCourseConfig().getStartLearning();
        return (current.after(start) && current.before(end));
    }

    @GetMapping("/course/{id}/posCode")
    public ResponseEntity<ServiceResult> getCoursePoscode(@PathVariable("id") Long id_course) {
        List<String> namePoscode = poscodeVnpostRepository.getNamePoscode(id_course);
        String names_course = courseService.findById(id_course).get().getName();
        Object[] objects = {names_course, namePoscode};
        return new ResponseEntity<>(new ServiceResult(objects, "Thành công", "200"), HttpStatus.OK);
    }

    @PostMapping("/course/course-category")
    public ResponseEntity<ServiceResult> getCourseInCategory(
            @RequestParam(name = "page", defaultValue = "1", required = false) Integer currentPage,
            @RequestParam(name = "size", defaultValue = "6", required = false) Integer size,
            @RequestBody CourseDTO courseDTO
            //  @PathVariable Long categoryId
    ) {
        Pageable pageable = PageRequest.of(currentPage - 1, size);
        // courseDTO.setCategoryId(categoryId);
        Long totalItem = courseProcessor.count(courseDTO);
        Integer totalPage = (int) Math.ceil((double) totalItem / size);
        List<CourseDTO> listData = courseProcessor.findAll(courseDTO, pageable);
        ServiceResult serviceResult = new ServiceResult(listData, totalPage, currentPage);
        return ResponseEntity.ok(serviceResult);
    }

    @PostMapping("/course")
    public ResponseEntity<ServiceResult> findAll(
            @RequestParam(name = "page", defaultValue = "1", required = false) Integer currentPage,
            @RequestParam(name = "size", defaultValue = "6", required = false) Integer size,
            @RequestBody CourseDTO courseDTO
    ) {
        Pageable pageable = PageRequest.of(currentPage > 0 ? currentPage - 1 : currentPage, size, Sort.by("id").descending());
        Long totalItem = courseProcessor.count(courseDTO);
        Integer totalPage = (int) Math.ceil((double) totalItem / size);
        List<CourseDTO> listData = courseProcessor.findAll(courseDTO, pageable);
        ServiceResult serviceResult = new ServiceResult(listData, totalPage, currentPage);
        return ResponseEntity.ok(serviceResult);
    }

    @GetMapping("/course/course-category/{id}")
    public ResponseEntity<ServiceResult> getCategory(@PathVariable("id") Long idCate) {
        return ResponseEntity.ok(new ServiceResult(categoryConverter.convertToDTO(d.findById(idCate).get()), "Thanh công", "200"));
    }

    @GetMapping("/sumary/finish/{id}")
    public ResponseEntity<Object[]> sumaryCourse(@PathVariable("id") Long id) throws CourseJoinException {
        MyUser user = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!courseJoinProcessor.exitsUserAndCourse(user.getId(), id)) {
            throw new CourseJoinException("Không có trong khóa học");
        }
        Course course = courseService.findById(id).get();
        List<Chapter> chapters = course.getOutline().getChapters();
        int soluong = courseService.getNumberChapterCourseWare(chapters);
        int sum = sohocllieuhoanthanh(chapters, user);
        CourseDTO courseDTO = converter.convertToDTO(course);
        StatusExamDTO statusExamDTO =new StatusExamDTO();
        List<Long> listIdRound = getListIdRound(course);
        if(listIdRound.size()>0){
            statusExamDTO =  competitionChapterService.setStatusExamDTO(listIdRound.get(0));
        }
        List<ChapterDTO> chapterDTOS = updateChapterComplete(chapterConverter.convertToList(chapters),user.getId());
        Object[] objects = {
                sum,  courseDTO,
                soluong,chapterDTOS, statusExamDTO
                // res[5 : sum ; res8 : soluong . res7 : courseDTO ,  res10 :chapterDTOS res23 :   statusExamDTO
        };
        return new ResponseEntity<Object[]>(objects, HttpStatus.OK);
    }



}