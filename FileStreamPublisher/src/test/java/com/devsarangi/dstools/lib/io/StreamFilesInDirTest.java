package com.devsarangi.dstools.lib.io;

import com.google.common.io.Files;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class StreamFilesInDirTest extends FileHelper {

    private StreamFilesInDir streamFilesInDir = null;

    @Before
    public void setup() {
        tempDir = Files.createTempDir();
        streamFilesInDir = new StreamFilesInDir();
    }

    @After
    public void cleanup() {
        tempDir.delete();
    }

    @Test
    public void streamsAllFiles() throws IOException {
        List<File> generateTestFiles = generateTestFiles(5);
        assertThat(generateTestFiles).hasSize(5);

        Stream<Path> pathStream = streamFilesInDir.filesInDir(tempDir.toPath());
        assertThat(pathStream.count()).isEqualTo(5L);
    }

    @Test
    public void streamsAllFilesWithinSubDirectories() throws IOException {
        List<File> generateTestFiles = generateTestFiles(5);
        assertThat(generateTestFiles).hasSize(5);

        List<File> generateTestFilesInSubDIr = generateTestFiles(5, true);

        Stream<Path> pathStream = streamFilesInDir.filesInDir(tempDir.toPath());
        pathStream.forEach(filePath -> System.out.println(filePath.getFileName()));
//        assertThat(pathStream.count()).isEqualTo(10L);
    }

}
