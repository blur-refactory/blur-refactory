package com.blur.bluruser.repository;

import com.blur.bluruser.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, String> {

    Report findByUserId(String userId);
    Report findByReportedUserId(String userId);
}
