package com.pyqportal.paper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaperRepository extends JpaRepository<Paper, UUID>, JpaSpecificationExecutor<Paper> {

    Optional<Paper> findByIdAndDeletedAtIsNull(UUID id);

    boolean existsByMd5Hash(String md5Hash);

    @Query("SELECT p FROM Paper p WHERE p.id IN :ids AND p.deletedAt IS NULL")
    List<Paper> findAllByIdsNotDeleted(@Param("ids") List<UUID> ids);

    @Modifying
    @Query("UPDATE Paper p SET p.downloadCount = p.downloadCount + 1 WHERE p.id = :id")
    void incrementDownloadCount(@Param("id") UUID id);
}
