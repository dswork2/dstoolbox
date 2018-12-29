package com.ds.toolbox.fileGroup.config;

import com.ds.toolbox.fileGroup.utils.FileInfoUtils;
import com.ds.toolbox.filePublisher.FileStreamGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@Slf4j
public class IntegrationConfig {

    @MessagingGateway
    public interface ScanningInputGateway {
        @Gateway(requestChannel = "inputGateway")
        Message startScan(String fileScanPath);
    }

    @Transformer(inputChannel = "inputGateway", outputChannel = "fileRecordChain")
    public Stream<File> generateFileStreamFromInputPath(String fileScanPath) {
        Stream<File> fileFlux = null;
        try {
            fileFlux = FileStreamGenerator.streamFilesFrom(fileScanPath, false);
        } catch (IOException e) {
            log.error("Cannot generate flux from path " + fileScanPath + " Error " + e.getMessage());
            if (log.isDebugEnabled()) {
                e.printStackTrace();
            }
        }
        return fileFlux;
    }

    @ServiceActivator(inputChannel = "fileRecordChain", outputChannel = "nullChannel")
    public void printFileInfo(Stream<File> fileStream) {
        log.info("Printing FileInfo ");

        List<FileDetails> filesreceived = fileStream
                .filter(FileInfoUtils.isImageFile)
                .map(file -> {
                    Path fileAsPath = Paths.get(file.getAbsolutePath());
                    FileDetails fileDetails = FileDetails.builder()
                            .fileName(file.getName())
                            .fileSizeInBytes(FileInfoUtils.getSize.apply(fileAsPath))
                            .path(file.getAbsolutePath())
                            .isImageFile(FileInfoUtils.isImageFile.test(file))
                            .metadata(new ImageMetadata(file))
                            .build();
                    log.info("Received file name " + fileDetails.fileName);
                    log.info("Received file metadata " + fileDetails.metadata.toString());
                    return fileDetails;
                })
                .collect(Collectors.toList());
    }

}
