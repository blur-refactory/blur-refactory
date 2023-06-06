package com.blur.bluruser.match.repository;

import com.blur.bluruser.match.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, String> {

    Report findByUserId(String userId);
    Report findByReportedUserId(String userId);
}
