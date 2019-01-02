package com.ds.toolbox.fileGroup.utils;

import com.drew.imaging.FileType;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifImageDirectory;
import com.drew.metadata.exif.ExifSubIFDDescriptor;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.jpeg.JpegDirectory;
import com.drew.metadata.png.PngDirectory;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public class ImageMetadataExtractorUtil {
    private enum Dimensions {
        HEIGHT,
        WIDTH;
    }

    public ImageMetadataExtractorUtil(File file) {
        super();
        metadata = metadataExtractor.apply(file);
        tags = allTagsExtractor.apply(metadata);
        fileType = FileInfoUtils.fileTypeFromFile.apply(file);
        exifSubIFDDirectory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
        jpegDirectory = metadata.getFirstDirectoryOfType(JpegDirectory.class);
        tifDirectory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
        imageDirectory = metadata.getFirstDirectoryOfType(ExifImageDirectory.class);
//        descriptor = new ExifSubIFDDescriptor(exifSubIFDDirectory);
//        jpegDescriptor = new JpegDescriptor(jpegDirectory);
    }

    private ExifSubIFDDirectory exifSubIFDDirectory;
    private JpegDirectory jpegDirectory;
    private ExifIFD0Directory tifDirectory;
    private ExifImageDirectory imageDirectory;
//    private ExifSubIFDDescriptor descriptor;
    //    private JpegDescriptor jpegDescriptor;
    private Metadata metadata;
    private Set<Tag> tags;
    private FileType fileType;

    private Function<Metadata, Set<Tag>> allTagsExtractor = metadata1 -> {
        Set<Tag> tags = Sets.newHashSet();
        Lists.newArrayList(metadata.getDirectories())
                .stream()
                .flatMap(directory -> directory.getTags().stream())
                .forEach(tag -> tags.add(tag));
        return tags;
    };

    // https://github.com/drewnoakes/metadata-extractor
    private static Function<File, Metadata> metadataExtractor = file -> {
        Metadata metadata = null;
        try {
            metadata = ImageMetadataReader.readMetadata(file);
        } catch (ImageProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return metadata;
    };


    @Override
    public String toString() {
        Map<String, String> tagsMap = tags.stream().collect(Collectors.toMap(Tag::getTagName, Tag::getDescription));
        return getString(tagsMap);
    }

    private String getString(Map<String, String> exifData) {
        ObjectMapper objectMapper = new ObjectMapper();

        String valueAsString = StringUtils.EMPTY;
        try {
            valueAsString = objectMapper.writeValueAsString(exifData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return valueAsString;
    }


    public String getDimension(FileType fileType, Dimensions dimensionType) {

        System.out.println("Checking filetype :" + fileType);
        Optional<Integer> value = Optional.empty();
        try {
            value = Optional.ofNullable(getExifDirectory(fileType).getInt(getTagType(fileType, dimensionType)));
        } catch (MetadataException e) {
            e.printStackTrace();
        }

        if (value.isPresent()) {
            return String.valueOf(value.get());
        } else {
            return "Not Found";
        }
    }

    private int getTagType(FileType fileType, Dimensions dimensionType) {
        int tagType = 0;
        switch (fileType) {
            case Jpeg:
                switch (dimensionType) {
                    case HEIGHT:
                        tagType = JpegDirectory.TAG_IMAGE_HEIGHT;
                        break;
                    case WIDTH:
                        tagType = JpegDirectory.TAG_IMAGE_WIDTH;
                        break;
                }
                break;
            case Tiff:
                switch (dimensionType) {
                    case HEIGHT:
                        tagType = ExifIFD0Directory.TAG_IMAGE_HEIGHT;
                        break;
                    case WIDTH:
                        tagType = ExifIFD0Directory.TAG_IMAGE_WIDTH;
                        break;
                }
                break;
            case Png:
                switch (dimensionType) {
                    case HEIGHT:
                        tagType = PngDirectory.TAG_IMAGE_HEIGHT;
                        break;
                    case WIDTH:
                        tagType = PngDirectory.TAG_IMAGE_WIDTH;
                        break;
                }
                break;
            default:
                break;
        }
        return tagType;
    }

    private Directory getExifDirectory(FileType fileType) {
        Directory foundDirectory = this.exifSubIFDDirectory;
        switch (fileType) {
            case Jpeg:
                foundDirectory = this.metadata.getFirstDirectoryOfType(JpegDirectory.class);
                break;
            case Tiff:
                foundDirectory = this.metadata.getFirstDirectoryOfType(ExifImageDirectory.class);
                break;
            case Png:foundDirectory = this.metadata.getFirstDirectoryOfType(PngDirectory.class);
                break;
            default:
                foundDirectory = this.metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
                break;
        }
        return foundDirectory;
    }


    public String getImageHeight() {
        return getDimension(this.fileType, Dimensions.HEIGHT);
    }

    public String getImageWidth() {
        return getDimension(this.fileType, Dimensions.WIDTH);
    }
}
