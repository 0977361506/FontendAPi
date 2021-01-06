package com.vnpost.elearning.api;


import com.vnpost.elearning.service.NewService;
import eln.common.core.entities.news.New;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/news")
public class NewsDetailApi {
    @Autowired
    NewService n;

    @GetMapping("/detail/{id}")
    public ResponseEntity<New> findbyid(@PathVariable("id") Long id) {

        New aNew = n.findById(id).get();
       // System.out.println(aNew.getTitle());
        if (aNew == null) {
//            System.out.println("User.java with id " + id + " not found");
            return new ResponseEntity<New>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<New>(aNew, HttpStatus.OK);
    }

    // lấy toàn bộ tin tức

    @GetMapping("/lists/{id}")
    public ResponseEntity<List<New>> findAll(@PathVariable("id") int id) {
        List<New> list = n.phantrang(2, id);
        if (list.isEmpty()) {
            return new ResponseEntity<List<New>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<New>>(list, HttpStatus.OK);
    }

    //
    @GetMapping("/category/{id}")
    public ResponseEntity<List<New>> findbyidCategory(@PathVariable("id") Long id) {

        List<New> list = n.findByIdCategory(id);


        return new ResponseEntity<List<New>>(list, HttpStatus.OK);
    }
}
