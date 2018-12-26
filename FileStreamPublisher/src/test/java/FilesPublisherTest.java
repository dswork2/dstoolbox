import com.google.common.io.Files;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class FilesPublisherTest extends FileHelper{
    FilesPublisher filesPublisher = null;
    @Before
    public void setup() {
        tempDir = Files.createTempDir();
        filesPublisher = new FilesPublisher();
    }
    @After
    public void cleanup(){
        tempDir.delete();
    }

    @Test
    public void testFluxPublishing() throws InterruptedException, IOException {
        generateTestFiles(5);
        generateTestFiles(5,true);
        filesPublisher
                .getFilesInDir(tempDir.getAbsolutePath())
        .subscribe(System.out::println);
    }

}