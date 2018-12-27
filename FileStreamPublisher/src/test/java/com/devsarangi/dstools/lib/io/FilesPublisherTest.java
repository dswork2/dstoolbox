package com.devsarangi.dstools.lib.io;

import com.google.common.io.Files;
import org.assertj.core.util.Lists;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.internal.runners.JUnit44RunnerImpl;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.fail;

public class FilesPublisherTest extends FileHelper {
    FilesPublisher filesPublisher = null;
    List<File> publishedFiles;
    String absolutePath;

    private TemporaryFolder tempDir;

    @Before
    public void setup() throws IOException {

        if (tempDir != null && tempDir.getRoot().exists()) {
            cleanupDirs(tempDir);
        }
        tempDir = new TemporaryFolder();
        tempDir.create();


        filesPublisher = new FilesPublisher(new FileStreamUtil());
        absolutePath =tempDir.getRoot().getAbsolutePath();
        publishedFiles = Lists.newArrayList();
    }

    @After
    public void cleanup() throws IOException {
        cleanupDirs(tempDir);
        publishedFiles.clear();
    }

    @Test
    public void testFluxPublishing_doesntIncludeDirectoriesByDefault() throws IOException, InterruptedException {

        generateTestFiles(tempDir,5);
        generateTestFiles(tempDir,5, true);

        filesPublisher
                .publishFilesFrom(absolutePath)
                .subscribe(publishedFiles::add);

        Thread.sleep(10L);
        assertThat(publishedFiles).hasSize(10);
    }

    @Test
    public void testFluxPublishing_includesDirectories() throws IOException, InterruptedException {
        System.out.println("Created " + absolutePath);

        generateTestFiles(tempDir,5);
        generateTestFiles(tempDir,5, true);

        filesPublisher
                .publishFilesFrom(absolutePath, true)
                .subscribe(publishedFiles::add);
        Thread.sleep(10L);
        assertThat(publishedFiles).hasSize(12);
    }
}