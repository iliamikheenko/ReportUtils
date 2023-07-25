package com.medicbk.reportutils.repository;

import com.medicbk.reportutils.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReportRepository extends JpaRepository<Report, UUID> {
    boolean existsByPatientId(Long patientId);
}