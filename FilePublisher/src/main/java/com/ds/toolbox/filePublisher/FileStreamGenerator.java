package com.ds.toolbox.filePublisher;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FileStreamGenerator {

    public static Stream<File> streamFilesFrom(String path, boolean includeDirs) throws IOException {
        return new FileStreamGenerator().fileStream(path,includeDirs);
    }

    protected Stream<Path> walkPath(String rootDir, boolean includeDirs) throws IOException {
        System.out.println("Getting files from : " + rootDir);
        Stream<Path> pathWalkStream = Files.walk(Paths.get(rootDir));
        if (includeDirs == false) {
            pathWalkStream = pathWalkStream
                    .filter(path1 -> path1.toFile().isFile());
        }
        return pathWalkStream;
    }

    protected Stream<Path> pathStream(String rootDir) throws IOException {
        return pathStream(rootDir, false);
    }

    protected Stream<Path> pathStream(String rootDir, boolean includeDirs) throws IOException {
        return walkPath(rootDir, includeDirs);
    }

    protected Stream<File> fileStream(String rootDir) throws IOException {
        return fileStream(rootDir, false);
    }

    protected Stream<File> fileStream(String rootDir, boolean includeDirs) throws IOException {
        Stream<Path> fileStream = walkPath(rootDir, includeDirs);
        return fileStream.map(path -> path.toFile());
    }
}
