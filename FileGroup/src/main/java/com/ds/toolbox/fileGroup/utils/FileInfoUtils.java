package com.ds.toolbox.fileGroup.utils;

import com.drew.imaging.FileType;
import javassist.NotFoundException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public class FileInfoUtils {

    private static final String MIME_PREFIX = "image";

    public static Function<String, FileType> fileTypeExtractor = extension -> {
        String fileExtension = extension;
        fileExtension = normalizeFileExtension(extension, fileExtension);
        final String searchString = fileExtension;
//        System.out.println("Searching file type "+searchString);
        return Arrays.stream(FileType.values()).filter(fileType -> fileType.getName().equalsIgnoreCase(searchString)).findFirst().orElseGet(()->FileType.Unknown);
    };

    private static String normalizeFileExtension(String extension, String fileExtension) {
        if(extension.equalsIgnoreCase("jpg")){
            fileExtension = "jpeg";
        }else if(extension.equalsIgnoreCase("tif")){
            fileExtension="tiff";
        }
        return fileExtension;
    }

    public static Function<String, FileType> fileTypeFromFileName = fsFile -> FileInfoUtils.fileTypeFromFile.apply(new File(fsFile));
    public static Function<File, FileType> fileTypeFromFile = fsFile -> {
        String extension = FilenameUtils.getExtension(fsFile.getName());
        return fileTypeExtractor.apply(extension);
    };

    public static Predicate<? super Path> isImageFilePath = path -> {
        boolean isImageType = false;
        Optional<String> probeContentType = null;
        try {
            probeContentType = Optional.ofNullable(Files.probeContentType(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (probeContentType.isPresent()) {
            isImageType = probeContentType.get().split("/")[0].toLowerCase().equals(MIME_PREFIX);
        }

        return isImageType;
    };
    public static Predicate<? super File> isImageFile = file -> FileInfoUtils.isImageFilePath.test(Paths.get(file.getAbsolutePath()));

    public static Function<String, Long> getSize = fileAbsolutePath -> {
        Long size = 0L;
        try {
            size = Files.size(Paths.get(fileAbsolutePath));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return Long.valueOf(size);
    };

    // http://johnbokma.com/java/obtaining-image-metadata.html
    public static IIOMetadata extractMetadata(File file) {
        IIOMetadata metadata = null;
        try {

            ImageInputStream iis = ImageIO.createImageInputStream(file);
            Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);

            if (readers.hasNext()) {

                // pick the first available ImageReader
                ImageReader reader = readers.next();

                // attach source to the reader
                reader.setInput(iis, true);

                // read metadata of first image
                metadata = reader.getImageMetadata(0);

//                String[] names = metadata.getMetadataFormatNames();
//                int length = names.length;
//                for (int i = 0; i < length; i++) {
//                    System.out.println( "Format name: " + names[ i ] );
//                    displayMetadata(metadata.getAsTree(names[i]));
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return metadata;
    }

}
