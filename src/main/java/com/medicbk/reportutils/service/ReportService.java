package com.medicbk.reportutils.service;

import com.desprice.medicbk.commons.si_model_dto.model.report_manager.response.PatientReportFile;
import com.medicbk.reportutils.model.Report;
import com.medicbk.reportutils.repository.ReportRepository;
import com.medicbk.reportutils.util.MultiKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static com.desprice.medicbk.commons.si_model_dto.model.ns.patient_summary.enums.FileReportType.HTML;
import static com.desprice.medicbk.commons.si_model_dto.model.ns.patient_summary.enums.FileReportType.XML;
import static com.desprice.medicbk.commons.si_model_dto.model.ns.patient_summary.enums.ProcessingStatus.READY;
import static java.time.temporal.ChronoUnit.HOURS;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;

    private final String URL_PREFIX = "http://localhost/report/summary/";
    private final String PATH_PREFIX = "report/summary/";

    public boolean isPatientExist(Long patientId){
        return reportRepository.existsByPatientId(patientId);
    }

    public void save(Report report){
        reportRepository.save(report);
    }

    public void createInstanceAndSave(UUID patientUUID, Long patientId, List<Long> nosologyIds, Path filePath){
        var currentTime = Instant.now();
        var reportFiles = List.of(new PatientReportFile(HTML, PATH_PREFIX + patientUUID + ".html", URL_PREFIX + patientUUID + ".html"),
                new PatientReportFile(XML, PATH_PREFIX + patientUUID + ".xml", URL_PREFIX + patientUUID + ".xml"));

        Report report = Report.builder()
                .uuid(patientUUID)
                .patientId(patientId)
                .status(READY)
                .files(reportFiles)
                .modified(currentTime)
                .expire(currentTime.plus(1, HOURS))
                .startProcessing(currentTime)
                .multiKey(new MultiKey(patientId, nosologyIds).getMD5sum())
                .build();

        save(report);
    }
}