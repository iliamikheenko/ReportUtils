package com.medicbk.reportutils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.desprice.medicbk.commons.si_model_dto.model.report_manager.response.PatientReportFile;
import com.medicbk.reportutils.dto.ReportDto;
import com.medicbk.reportutils.model.Report;
import com.medicbk.reportutils.parser.XmlParser;
import com.medicbk.reportutils.service.ReportService;
import com.medicbk.reportutils.util.MultiKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.desprice.medicbk.commons.si_model_dto.model.ns.patient_summary.enums.FileReportType.HTML;
import static com.desprice.medicbk.commons.si_model_dto.model.ns.patient_summary.enums.FileReportType.XML;
import static com.desprice.medicbk.commons.si_model_dto.model.ns.patient_summary.enums.ProcessingStatus.READY;
import static java.time.temporal.ChronoUnit.HOURS;

@Component
@RequiredArgsConstructor
public class DirectoryProcessor {

    private final XmlParser xmlParser;
    private final ReportService reportService;

    private Path sourcePath;
    private Path newDirPath;

    public void processXmlFilesInDirectory(String directoryPath) {
        sourcePath = Path.of(directoryPath);
        try (var files = Files.walk(sourcePath)) {
            createNewDirectory();
                    files
                    .filter(Files::isRegularFile)
                    .filter(this::isXmlFile)
                    .forEach(this::processXmlFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isXmlFile(Path filePath) {
        String fileName = filePath.getFileName().toString();
        return fileName.endsWith(".xml");
    }

    private void processXmlFile(Path filePath) {
        try {
            String content = Files.readString(filePath);
            ReportDto reportDto = xmlParser.extractInfoFromXml(content);
            var patientId = reportDto.getPatientId();
            var patientUUID = reportDto.getUuid();
            var nosologyIds = reportDto.getNosologyIds();

            boolean patientExists = reportService.isPatientExist(patientId);
            if (!patientExists) {
                Report report = createInstance(patientUUID, patientId, nosologyIds,filePath);
                reportService.save(report);

                Files.copy(filePath, Path.of(newDirPath +"/" + filePath.getFileName()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Report createInstance(UUID patientUUID, Long patientId, List<Long> nosologyIds, Path filePath){
        var currentTime = Instant.now();
        return Report.builder()
                .uuid(patientUUID)
                .patientId(patientId)
                .status(READY)
                //todo implement what to save in path and url
                .files(List.of(new PatientReportFile(XML,filePath.toString(), "url"),
                        new PatientReportFile(HTML,filePath.toString(), "url")))
                .modified(currentTime)
                .expire(currentTime.plus(1, HOURS))
                .startProcessing(currentTime)
                .multiKey(new MultiKey(patientId,nosologyIds).getMD5sum())
                .build();
    }

    private void createNewDirectory() throws IOException {
        Path parentDir = sourcePath.getParent();
        String newDirectoryName = sourcePath.getFileName() + "_upgraded";
        newDirPath = parentDir.resolve(newDirectoryName);
        Files.createDirectories(newDirPath);
    }
}
