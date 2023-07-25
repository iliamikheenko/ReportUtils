package com.medicbk.reportutils.service;

import com.medicbk.reportutils.model.Report;
import com.medicbk.reportutils.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;

    public boolean isPatientExist(Long patientId){
        return reportRepository.existsByPatientId(patientId);
    }

    public void save(Report report){
        reportRepository.save(report);
    }
}
