package com.vnpost.elearning.api;

import com.vnpost.elearning.Beans.Dates;
import com.vnpost.elearning.converter.NewCategoryConverter;
import com.vnpost.elearning.converter.NewsConverter;
import com.vnpost.elearning.dto.EventDTO;
import com.vnpost.elearning.dto.NewCategoryDTO;
import com.vnpost.elearning.dto.NewsDTO;
import com.vnpost.elearning.dto.ServiceResult;
import com.vnpost.elearning.processor.NewsProcessor;
import com.vnpost.elearning.service.NewCategoryService;
import com.vnpost.elearning.service.NewService;
import eln.common.core.entities.news.New;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class NewsApi {
    @Autowired
    Dates d;
    @Autowired
    NewService newService;
    @Autowired
    NewCategoryService newcategory;
    @Autowired
    private NewsProcessor newsProcessor;
    @Autowired
    private NewsConverter newsConverter;
    @Autowired
    private NewCategoryConverter newCategoryConverter;




    @PostMapping("/home/news")
    public ResponseEntity<?>  getList(@RequestBody NewsDTO newsDTO){
        Pageable pageable = PageRequest.of(newsDTO.getPage() > 0 ? newsDTO.getPage() - 1 : 0, newsDTO.getMaxPageItems());
        Object[] objects = newService.getListNew(newsDTO,pageable);
        return new ResponseEntity<>(  new ServiceResult((List<EventDTO>) objects[0], Long.parseLong(objects[1].toString()),"Thanh cong","200"), HttpStatus.OK);
    }

    @GetMapping("/new/Category/{id}")
    public List<NewsDTO> getListNewsByCategory(@PathVariable("id") Long id){
        List<New> news = newService.findByIdCategory(id);
        List<NewsDTO> newsDTOS= new ArrayList<>();
        for(New n : news){
            newsDTOS.add(newsConverter.convertToDTO(n));
        }
        return  newsDTOS;
    }

    @GetMapping("/new/chitiet/{id}") // chi tiết bài viêt
    public ResponseEntity<Object[]> chitiet(@PathVariable("id") Long id) {
        NewsDTO news = newsConverter.convertToDTO(newService.findById(id).get());
        List<NewCategoryDTO> list = newcategory.findParent();
        List<NewsDTO>  listnewsBest = newService.getListNewBest();
        Object[] objects = {list,listnewsBest,news};
        return new ResponseEntity<Object[]>(objects, HttpStatus.OK);
    }



    @GetMapping("/news/all")
    public ResponseEntity<ServiceResult> getAll(){
        List<NewsDTO> listNews = newsProcessor.findAll();
        ServiceResult serviceResult = new ServiceResult(listNews,"Danh sách tin tức","200");
        return ResponseEntity.ok(serviceResult);
    }
    @GetMapping("/news/{id}")
    public ResponseEntity<ServiceResult> getDetail(@PathVariable Long id){
        NewsDTO newsDTO = newsProcessor.findById(id);
        ServiceResult serviceResult = new ServiceResult(newsDTO,"Chi tiết tin tức","200");
        return ResponseEntity.ok(serviceResult);
    }

    @GetMapping("/news/categories/list")
    public ResponseEntity<ServiceResult> getCategories() {
        List<NewCategoryDTO> categories = newcategory.findParent();
        return ResponseEntity.ok(new ServiceResult(categories,"success","200"));
    }

    @GetMapping("/news/categories/{id}/list")
    public ResponseEntity<ServiceResult> findListByParent(@PathVariable("id") Long id) {
        List<NewCategoryDTO> categories = newcategory.findByParent(id);
        return ResponseEntity.ok(new ServiceResult(categories,"success","200"));
    }
    @GetMapping("/news/categories/{id}")
    public ResponseEntity<ServiceResult> findByid(@PathVariable("id") Long id) {
        NewCategoryDTO  newCategoryDTO = newCategoryConverter.convertToDTO(newcategory.findById(id).get());
        return ResponseEntity.ok(new ServiceResult(newCategoryDTO,"success","200"));
    }
}
