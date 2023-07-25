package com.medicbk.reportutils;

import com.medicbk.reportutils.util.DirectoryUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class ReportUtilsApplication {

    private final DirectoryUtils directoryUtils;

    @Value("${source.path}")
    private String folderPath;

    public static void main(String[] args) {
        var context = SpringApplication.run(ReportUtilsApplication.class, args);
        var app = context.getBean(ReportUtilsApplication.class);
        app.processXmlFilesInDirectory();

    }
    private void processXmlFilesInDirectory() {
        directoryUtils.processXmlFilesInDirectory(folderPath);
    }
}
