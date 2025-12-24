package com.example.backend.controller;


import com.example.backend.entity.UploadedFile;
import com.example.backend.service.FileFetchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileFetchController {

    @Autowired
    private FileFetchService fileFetchService;

    @GetMapping("/papers")
    public List<UploadedFile> getPapers(@RequestParam(required = false) String courseName , @RequestParam(required = false) String year ,@RequestParam(required = false) String semester){
        return fileFetchService.getPapers(courseName, year , semester);
    }
}
