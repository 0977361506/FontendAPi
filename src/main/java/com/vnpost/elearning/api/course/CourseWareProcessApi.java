package com.vnpost.elearning.api.course;

import com.vnpost.elearning.dto.course.CourseProgressDTO;
import com.vnpost.elearning.dto.course.CourseWareProcessDTO;
import com.vnpost.elearning.dto.ServiceResult;
import com.vnpost.elearning.processor.CourseJoinProcessor;
import com.vnpost.elearning.processor.CourseWareProgressProcessor;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author:Nguyen Anh Tuan
 *     <p>June 09,2020
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/course-ware-process")
public class CourseWareProcessApi {
  private CourseWareProgressProcessor courseWareProgressProcessor;
  private CourseJoinProcessor courseJoinProcessor;

  @GetMapping("/{courseWareId}/{chapterId}")
  public ResponseEntity<ServiceResult> save(
      @PathVariable Long courseWareId, @PathVariable(name = "chapterId") Long chapterId) {
    ServiceResult serviceResult = new ServiceResult();

    try {
      //            courseWareProgressProcessor.saveProcess(courseWareId, chapterId);
      //      courseJoinProcessor.updateProcess(chapterId);
      serviceResult.setCode("200");
      //        } catch (CourseJoinException e) {
      //            serviceResult.setCode("500");
      //            return new ResponseEntity<>(serviceResult, HttpStatus.BAD_REQUEST);
      //        }
    } catch (Exception e) {

    }
    return ResponseEntity.ok(serviceResult);
  }

  @GetMapping("/course/{courseId}")
  public ResponseEntity<ServiceResult> getListSeen(@PathVariable Long courseId) {
    ServiceResult serviceResult = new ServiceResult("Danh sách học liệu đã xem", "200");
    List<Long> data = courseWareProgressProcessor.getListSeenByCourseId(courseId);
    serviceResult.setData(data);
    return ResponseEntity.ok(serviceResult);
  }

  @PutMapping
  public ResponseEntity<ServiceResult> update(@RequestBody CourseProgressDTO courseProgressDTO) {
    courseWareProgressProcessor.listenUpdateProgress(courseProgressDTO);
    courseJoinProcessor.updateProgress(
        courseProgressDTO.getIdChapter(), courseProgressDTO.getUserName());
    return ResponseEntity.ok(new ServiceResult());
  }

  @PostMapping
  public ResponseEntity<ServiceResult> create(@RequestBody CourseProgressDTO courseProgressDTO) {
    courseWareProgressProcessor.listenCreateProgress(courseProgressDTO);
      courseJoinProcessor.updateProgress(
              courseProgressDTO.getIdChapter(), courseProgressDTO.getUserName());
    return ResponseEntity.ok(new ServiceResult());
  }

  @GetMapping("/{courseWareId}/{chapterId}/process")
  public ResponseEntity<ServiceResult> getProcess(
          @PathVariable Long courseWareId, @PathVariable(name = "chapterId") Long chapterId) {
    CourseWareProcessDTO result = courseWareProgressProcessor.getProcess(courseWareId, chapterId);
    return ResponseEntity.ok(new ServiceResult(result, "success", "200"));
  }

  @GetMapping
  public ResponseEntity<ServiceResult> checkFinishCourse(
          @RequestParam(required = false,name = "userName")  String userName
          ,@RequestParam(required = false,name = "idCourseWare")  Long idCourseWare
          ,@RequestParam(required = false,name = "idChapter")  Long idChapter) {
    return ResponseEntity.ok(new ServiceResult(courseWareProgressProcessor.checkFinishCourse(userName,idChapter,idCourseWare), "success", "200"));
  }
}
