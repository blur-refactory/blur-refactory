package com.blur.blurmatch.repository;

import com.blur.blurmatch.entity.MatchMakingRating;
import com.blur.blurmatch.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, String> {

    Report findByUserId(String userId);
    Report findByReportedUserId(String userId);
}
