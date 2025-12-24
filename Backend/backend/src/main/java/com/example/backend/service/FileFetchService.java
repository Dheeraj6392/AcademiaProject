package com.example.backend.service;

import com.example.backend.entity.UploadedFile;
import com.example.backend.entity.UploadStatus;
import com.example.backend.repository.UploadedFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileFetchService {

    @Autowired
    private UploadedFileRepository repository;

    public List<UploadedFile> getPapers(String courseName , String year , String semester){
        return repository.findPapers(courseName , year , semester, UploadStatus.APPROVED);
    }
}
