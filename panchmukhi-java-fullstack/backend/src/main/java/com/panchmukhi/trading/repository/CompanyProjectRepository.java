package com.panchmukhi.trading.repository;

import com.panchmukhi.trading.model.CompanyProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyProjectRepository extends JpaRepository<CompanyProject, String> {

    List<CompanyProject> findByCompanyNameContainingIgnoreCase(String companyName);

    List<CompanyProject> findBySectorOrderByAnnouncedDateDesc(String sector);

    List<CompanyProject> findByProjectTypeOrderByAnnouncedDateDesc(String projectType);

    List<CompanyProject> findByStatusOrderByAnnouncedDateDesc(String status);

    List<CompanyProject> findTop10ByOrderByAnnouncedDateDesc();
}
