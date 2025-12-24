package com.example.backend.repository;

import com.example.backend.entity.UploadedFile;
import com.example.backend.entity.UploadStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UploadedFileRepository extends JpaRepository<UploadedFile, Long> {

    @Query("""
    SELECT f FROM UploadedFile f
    WHERE f.status = :status
      AND (:courseName IS NULL OR f.courseName = :courseName)
      AND (:year IS NULL OR f.year = :year)
      AND (:semester IS NULL OR f.semester = :semester)
    ORDER BY f.uploadedAt DESC
    """)
    List<UploadedFile> findPapers(
            @Param("courseName") String courseName,
            @Param("year") String year,
            @Param("semester") String semester,
            @Param("status") UploadStatus status
    );

    List<UploadedFile> findByStatusOrderByUploadedAtDesc(UploadStatus status);
}
