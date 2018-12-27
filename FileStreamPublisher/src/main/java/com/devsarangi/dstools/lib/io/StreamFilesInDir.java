package com.devsarangi.dstools.lib.io;

import java.nio.file.Path;
import java.util.stream.Stream;

import static java.util.Collections.singletonList;

public class StreamFilesInDir implements FilesScanner {

    public Stream<Path> filesInDir(Path dir) {
        return listFiles(dir)
                .flatMap(path ->
                        path.toFile().isDirectory() ?
                                filesInDir(path) :
                                singletonList(path).stream());
    }


}
