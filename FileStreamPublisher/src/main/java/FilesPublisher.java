import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.io.File;
import java.nio.file.Path;
import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.stream.Collectors;

public class FilesPublisher implements FilesScanner {

    public Flux<Path> getFilesInDir(String path) throws InterruptedException {

        Path filePath = new File(path).toPath();
        return Flux.fromIterable(listFiles(filePath).collect(Collectors.toList()))
                .expand(p ->
                    Flux.fromIterable(
                                listFiles(p).collect(Collectors.toList())
                        ).subscribeOn(Schedulers.elastic())
                )
                .publishOn(Schedulers.elastic());
    }
}
