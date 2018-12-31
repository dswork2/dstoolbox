package com.ds.toolbox.fileGroup.config;

import com.ds.toolbox.fileGroup.utils.FileInfoUtils;
import com.ds.toolbox.filePublisher.FileStreamGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.*;
import org.springframework.messaging.Message;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Configuration
@Slf4j
public class IntegrationConfig {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class FileDetailsDto {
        File file;
        FileDetails fileDetails;
    }

    @MessagingGateway
    public interface ScanningInputGateway {
        @Gateway(requestChannel = "inputGateway")
        Message startScan(String fileScanPath);
    }

    @Transformer(inputChannel = "inputGateway", outputChannel = "fileDetailsBuilder")
    public Stream<File> generateFileStreamFromInputPath(String fileScanPath) {
        Stream<File> fileStream = null;
        try {
            fileStream = FileStreamGenerator.streamFilesFrom(fileScanPath, false);
        } catch (IOException e) {
            log.error("Cannot generate stream from path " + fileScanPath + " Error " + e.getMessage());
            if (log.isDebugEnabled()) {
                e.printStackTrace();
            }
        }
        return fileStream;
    }

    @Transformer(inputChannel = "fileDetailsBuilder", outputChannel = "imageFileFilterStream")
    public Stream<FileDetailsDto> printFileInfo(Stream<File> fileStream) {
        log.info("Printing FileInfo ");

        return fileStream
                .filter(FileInfoUtils.isImageFile)
                .map(file -> {
                    Path fileAsPath = Paths.get(file.getAbsolutePath());
                    FileDetails fileDetails = FileDetails.builder()
                            .fileName(file.getName())
                            .fileSizeInBytes(FileInfoUtils.getSize.apply(fileAsPath))
                            .path(file.getAbsolutePath())
                            .build();
//                    log.info("Received file name " + fileDetails.fileName);

                    return new FileDetailsDto(file,fileDetails);
                });
    }

    @Transformer(inputChannel = "imageFileFilterStream", outputChannel = "imageFileChannel")
    public Stream<FileDetailsDto> filterImageFiles(Stream<FileDetailsDto> receivedFileStream){
        return receivedFileStream
                .filter(fileDetailsDto -> FileInfoUtils.isImageFile.test(fileDetailsDto.file));
    }

    @Transformer(inputChannel = "imageFileChannel", outputChannel = "processingChannel")
    public Stream<FileDetails> getMetadata(Stream<FileDetailsDto> fileDetailsStream){
        return fileDetailsStream
                .map(fileDetailsDto -> {
                    FileDetails fileDetails = fileDetailsDto.fileDetails;
                    fileDetails.setImageFileIndicator(true);
                    fileDetails.setMetadata(new ImageMetadata(fileDetailsDto.file));
                    return fileDetails;
                });
    }

    @ServiceActivator(inputChannel = "processingChannel", outputChannel = "nullChannel")
    public void processImageFile(Stream<FileDetails> fileDetailsStream){
        fileDetailsStream.forEach(fileDetails -> log.info("Metadata " + fileDetails.metadata + " / " + fileDetails.fileName));
    }

}
