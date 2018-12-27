package com.devsarangi.dstools.lib.io;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;
import java.util.stream.Stream;

public class FilesPublisher implements FilesScanner {

    public Flux<File> getFilesInDir(String path) throws IOException {
        return getFilesInDir(path, false);
    }

    public Flux<File> getFilesInDir(String path, boolean includeDirs) throws IOException {
        Stream<Path> fileStream = Files
                .walk(Paths.get(path));
        if (includeDirs == false) {
            fileStream = fileStream
                    .filter(path1 -> path1.toFile().isFile());
        }

        return getFluxFromStream(fileStream);
    }

    public Flux<File> getFileDtoInDir(String path, Function<File, ? extends Object> fileToDtoConverter) throws IOException {
        Stream<Path> streamOfFiles = Files
                .walk(Paths.get(path))
                .filter(path1 -> path1.toFile().isFile());

        return getFluxFromStream(streamOfFiles).doOnNext(file -> fileToDtoConverter.apply(file));
    }


    private Flux<File> getFluxFromStream(Stream<Path> streamOfFiles) {
        return Flux.fromStream(
                streamOfFiles
                        .map(path -> path.toFile()))
                .subscribeOn(Schedulers.elastic())
                .onErrorStop()
                .publishOn(Schedulers.elastic());
    }
}
