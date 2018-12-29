package com.ds.toolbox.filePublisher;

import org.assertj.core.util.Lists;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class FilesFluxGeneratorTest extends FileHelper {
    FilesFluxGenerator filesFluxGenerator = null;
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


        filesFluxGenerator = new FilesFluxGenerator(new FileStreamGenerator());
        absolutePath = tempDir.getRoot().getAbsolutePath();
        publishedFiles = Lists.newArrayList();
    }

    @After
    public void cleanup() throws IOException {
        cleanupDirs(tempDir);
        publishedFiles.clear();
    }

    @Test
    public void testFluxPublishing_doesntIncludeDirectoriesByDefault() throws IOException, InterruptedException {

        generateTestFiles(tempDir, 5);
        generateTestFiles(tempDir, 5, true);

        filesFluxGenerator
                .publishFilesFrom(absolutePath)
                .subscribe(publishedFiles::add);

        Thread.sleep(10L);
        assertThat(publishedFiles).hasSize(10);
    }

    @Test
    public void testFluxPublishing_includesDirectories() throws IOException, InterruptedException {
        System.out.println("Created " + absolutePath);

        generateTestFiles(tempDir, 5);
        generateTestFiles(tempDir, 5, true);

        filesFluxGenerator
                .publishFilesFrom(absolutePath, true)
                .subscribe(publishedFiles::add);
        Thread.sleep(10L);
        assertThat(publishedFiles).hasSize(12);
    }
}