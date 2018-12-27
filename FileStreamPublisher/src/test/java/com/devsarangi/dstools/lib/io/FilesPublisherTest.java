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
    @Before
    public void setup() {
        tempDir = Files.createTempDir();
        if(tempDir.exists()){
            tempDir.delete();
            tempDir = Files.createTempDir();
        }
        filesPublisher = new FilesPublisher();
    }
    @After
    public void cleanup(){
        tempDir.delete();
    }

    @Test
    public void testFluxPublishing() throws IOException {
        String tempDirAbsolutePath = tempDir.getAbsolutePath();
        System.out.println(tempDirAbsolutePath);
        generateTestFiles(5);
        generateTestFiles(5,true);
        List<File> publishedFiles = new ArrayList<>();

        filesPublisher
                .getFilesInDir(tempDirAbsolutePath)
                .subscribe(publishedFiles::add);

        assertThat(publishedFiles).hasSize(10);
        publishedFiles.clear();
        filesPublisher
                .getFilesInDir(tempDirAbsolutePath, true)
                .subscribe(publishedFiles::add);
        assertThat(publishedFiles).hasSize(12);
    }

}