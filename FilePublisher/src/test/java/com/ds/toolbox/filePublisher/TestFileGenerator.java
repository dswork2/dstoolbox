package com.ds.toolbox.filePublisher;

import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TestFileGenerator {
    protected List<File> generateTestFiles(TemporaryFolder tempDir, int numberOfFiles) throws IOException {
        return generateTestFiles(tempDir, numberOfFiles, false);
    }

    protected List<File> generateTestFiles(TemporaryFolder tempDir, int numberOfFiles, boolean subDir) throws IOException {

        List<File> generatedFiles = new ArrayList<>();
        File dir2=null;
        if(subDir){
            dir2 = tempDir.newFolder("subDir");
        }
        while (numberOfFiles > 0) {
            if(subDir){
                System.out.println("Generating files in subDir " + dir2.getAbsolutePath());
                generatedFiles.add(File.createTempFile("temp_","",dir2));

            }else{
                System.out.println("Generating files in " + tempDir.getRoot().getAbsolutePath());
                generatedFiles.add(tempDir.newFile("temp_"+numberOfFiles+".txt"));
            }
            numberOfFiles--;
        }
        return generatedFiles;
    }

    protected void cleanupDirs(TemporaryFolder tempDir) throws IOException {
        Files.walk(Paths.get(tempDir.getRoot().getAbsolutePath()))
                .filter(path -> path.toFile().isDirectory() == false)
                .forEach(path -> path.toFile().delete());

        Files.walk(Paths.get(tempDir.getRoot().getAbsolutePath()))
                .filter(path -> path.toFile().isDirectory())
                .forEach(path -> path.toFile().delete());

        tempDir.delete();

    }
}
