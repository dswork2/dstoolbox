package com.devsarangi.dstools.lib.io;

import com.google.common.io.Files;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class FilesPublisherTest extends FileHelper{
    FilesPublisher filesPublisher = null;
    String tempDirAbsolutePath;
    List<File> publishedFiles;

    @Before
    public void setup() {
        tempDir = Files.createTempDir();
        tempDirAbsolutePath = tempDir.getAbsolutePath();
        publishedFiles = new ArrayList<>();

        filesPublisher = new FilesPublisher();
    }

    @After
    public void cleanup(){
        if(tempDir.exists()) {
            tempDir.delete();
        }
        publishedFiles.clear();
    }

    @Test
    public void testFluxPublishing_doesntIncludeDirectoriesByDefault() throws IOException {

        generateTestFiles(5);
        generateTestFiles(5,true);

        filesPublisher
                .getFilesInDir(tempDirAbsolutePath)
                .subscribe(publishedFiles::add);

        assertThat(publishedFiles).hasSize(10);
    }

    @Test
    public void testFluxPublishing_includesDirectories() throws IOException {
        generateTestFiles(5);
        generateTestFiles(5,true);

        filesPublisher
                .getFilesInDir(tempDirAbsolutePath, true)
                .subscribe(publishedFiles::add);

        assertThat(publishedFiles).hasSize(12);
    }


}