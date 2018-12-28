package com.ds.toolbox.filePublisher;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Function;
import java.util.stream.Stream;

public class FilesPublisher {
    private final FileStreamUtil fileStreamUtil;

    public FilesPublisher() {
        this(new FileStreamUtil());
    }

    public FilesPublisher(FileStreamUtil fileStreamUtil) {
        this.fileStreamUtil = fileStreamUtil;
    }

    public Flux<File> publishFilesFrom(String path) throws IOException {
        return publishFilesFrom(path, false);
    }

    public Flux<File> publishFilesFrom(String rootDir, boolean includeDirs) throws IOException {
        Stream<Path> fileStream = fileStreamUtil.pathStream(rootDir, includeDirs);
        return fluxGenerator.apply(fileStream);
    }

    private Function<Stream<Path>, Flux<File>> fluxGenerator =
            pathStream ->
                    Flux.fromStream(pathStream.map(path -> path.toFile()))
//                            .doOnNext(v -> System.out.println("before publishOn: " + v.getAbsolutePath()))
                            .publishOn(Schedulers.elastic())
//                            .doOnNext(v -> System.out.println("after publishOn: " + v.getAbsolutePath()))
                            .subscribeOn(Schedulers.elastic());


    /***
     * Flux.just("a", "b", "c") //this is where subscription triggers data production
     *         //this is influenced by subscribeOn
     *         .doOnNext(v -> System.out.println("before publishOn: " + Thread.currentThread().getName()))
     *         .publishOn(Schedulers.elastic())
     *         //the rest is influenced by publishOn
     *         .doOnNext(v -> System.out.println("after publishOn: " + Thread.currentThread().getName()))
     *         .subscribeOn(Schedulers.parallel())
     *         .subscribe(v -> System.out.println("received " + v + " on " + Thread.currentThread().getName()));
     *     Thread.sleep(5000);
     */
}
