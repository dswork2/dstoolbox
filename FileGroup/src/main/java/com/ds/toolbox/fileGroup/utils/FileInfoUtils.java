package com.ds.toolbox.fileGroup.utils;

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
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public class FileInfoUtils {
    private static String mimePrefix = "image";

    public static Predicate<? super File> isImageFile = file -> FileInfoUtils.isImageFilePath.test(Paths.get(file.getAbsolutePath()));

    public static Predicate<? super Path> isImageFilePath = path -> {
        boolean isImageType = false;
        Optional<String> probeContentType = null;
        try {
            probeContentType = Optional.ofNullable(Files.probeContentType(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (probeContentType.isPresent()) {
            isImageType = probeContentType.get().split("/")[0].toLowerCase().equals(mimePrefix);
        }

        return isImageType;
    };

    public static Function<Path, Long> getSize = path -> {
        Long size = 0L;
        try {
            size = Files.size(path);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return Long.valueOf(size);
    };

    public static Function<Path, String> toFileType = fsFilePath -> FilenameUtils.getExtension(fsFilePath.toFile().getName());
    public static Function<Path, String> toFileName = fsFilePath -> fsFilePath.toFile().getName();


    // http://johnbokma.com/java/obtaining-image-metadata.html
    public static IIOMetadata extractMetadata(File file ) {
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
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return metadata;
    }

}
