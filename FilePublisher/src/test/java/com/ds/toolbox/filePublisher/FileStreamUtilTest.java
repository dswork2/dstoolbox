package com.ds.toolbox.filePublisher;

import com.ds.toolbox.FileStreamUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(JUnit4.class)
public class FileStreamUtilTest extends FileHelper {

    private FileStreamUtil fileStreamUtil = null;

    private TemporaryFolder tempDir;

    @Before
    public void setup() throws IOException {

        if (tempDir != null && tempDir.getRoot().exists()) {
            cleanupDirs(tempDir);
        }
        tempDir = new TemporaryFolder();
        tempDir.create();


        fileStreamUtil = new FileStreamUtil();
    }

    @After
    public void cleanup() throws IOException {
        cleanupDirs(tempDir);
    }
    @Test
    public void streamsAllFiles() throws IOException {
        List<File> testFiles = generateTestFiles(tempDir, 5);

        assertThat(testFiles).hasSize(5);

        Stream<Path> pathStream = fileStreamUtil.pathStream(tempDir.getRoot().getAbsolutePath());
        assertThat(pathStream.count()).isEqualTo(5L);
    }

    @Test
    public void streamsAllFilesWithinSubDirectories() throws IOException {
        List<File> generatedTestFiles = generateTestFiles(tempDir, 5);
        generatedTestFiles.addAll(generateTestFiles(tempDir, 5, true));

        System.out.println("Generated : "+ generatedTestFiles.size()+" files");

        Stream<Path> pathStream = fileStreamUtil.pathStream(tempDir.getRoot().getAbsolutePath(),true);
        List<File> testFiles = pathStream.map(path -> path.toFile()).collect(Collectors.toList());
        testFiles.stream().forEach(file ->
                System.out.println("Got file"+ file.getAbsolutePath())
        );

        assertThat(testFiles).hasSize(12);
    }

}
