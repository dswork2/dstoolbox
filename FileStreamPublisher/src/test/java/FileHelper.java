import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileHelper {
    protected File tempDir;

    protected File createTestFile(boolean subDirectory) {
        File tempFile = null;
        try {
            String dirPath = tempDir.getAbsolutePath();
            File subDir = new File(dirPath + File.separator + "subDir");
            subDir.mkdir();
            tempFile = subDirectory ? File.createTempFile("test", "", tempDir)
                    : File.createTempFile("test", "", subDir);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempFile;
    }

    protected List<File> generateTestFiles() {
        return generateTestFiles(1, false);
    }

    protected List<File> generateTestFiles(int numberOfFiles) throws IOException {
        return generateTestFiles(numberOfFiles, false);
    }

    protected List<File> generateTestFiles(int numberOfFiles, boolean subDir) {
        List<File> generatedFiles = new ArrayList<>();
        while (numberOfFiles > 0) {
            File testFile = createTestFile(subDir);
            generatedFiles.add(testFile);
            numberOfFiles--;
        }
        return generatedFiles;
    }
}
