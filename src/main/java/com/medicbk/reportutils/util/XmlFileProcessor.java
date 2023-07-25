package com.medicbk.reportutils.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import com.medicbk.reportutils.dto.ReportDto;
import com.medicbk.reportutils.parser.XmlParser;
import com.medicbk.reportutils.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.medicbk.reportutils.util.DirectoryUtils.newDirPath;

@Slf4j
@Component
@RequiredArgsConstructor
public class XmlFileProcessor {
    private final XmlParser xmlParser;
    private final ReportService reportService;

    /**
     * Process an XML file, extract information from it,
     * and save the data to the database.
     *
     * @param filePath The path to the XML file to be processed.
     */
    public void processXmlFile(Path filePath) {
        try {
            String content = Files.readString(filePath);
            var reportOpt = xmlParser.extractInfoFromXml(content);

            if (reportOpt.isPresent()){
                var  reportDto = reportOpt.get();
                var patientId = reportDto.getPatientId();
                var patientUUID = reportDto.getUuid();
                var nosologyIds = reportDto.getNosologyIds();

                boolean patientExists = reportService.isPatientExist(patientId);
                if (!patientExists) {
                    reportService.createInstanceAndSave(patientUUID, patientId, nosologyIds,filePath);

                    Files.copy(filePath, Path.of(newDirPath +"/" + filePath.getFileName()));
                } else {
                log.warn("Patient " + patientId + " already exists in database.");
                }
            } else {
                log.error("XML parsing returned no data. File: " + filePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}