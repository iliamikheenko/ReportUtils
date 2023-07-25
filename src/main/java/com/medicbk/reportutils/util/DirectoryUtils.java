package com.medicbk.reportutils.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
@RequiredArgsConstructor
public class DirectoryUtils {
    private final XmlFileProcessor xmlFileProcessor;


    private Path sourcePath;
    public static Path newDirPath;

    /**
     * Processes XML files in the specified directory.
     * @param directoryPath
     */
    public void processXmlFilesInDirectory(String directoryPath) {
        sourcePath = Path.of(directoryPath);
        try (var files = Files.walk(sourcePath)) {
            createNewDirectory();
            files
                    .filter(Files::isRegularFile)
                    .filter(this::isXmlFile)
                    .forEach(xmlFileProcessor::processXmlFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createNewDirectory() throws IOException {
        Path parentDir = sourcePath.getParent();
        String newDirectoryName = sourcePath.getFileName() + "_upgraded";
        newDirPath = parentDir.resolve(newDirectoryName);
        Files.createDirectories(newDirPath);
    }

    private boolean isXmlFile(Path filePath) {
        String fileName = filePath.getFileName().toString();
        return fileName.endsWith(".xml");
    }
}