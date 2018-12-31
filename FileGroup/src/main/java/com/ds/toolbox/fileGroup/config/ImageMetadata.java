package com.ds.toolbox.fileGroup.config;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;

import java.beans.Transient;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class ImageMetadata {

    private Map<String, String> exifData;
    private Metadata metadata;
    private Set<Tag> tags;

    public ImageMetadata(File file) {
        super();
        this.metadata = metadataExtractor.apply(file);

        this.tags = Sets.newConcurrentHashSet();
        populateTags(this.metadata, this.tags);

        this.exifData = Maps.newHashMap();
        populateExifData(this.metadata, this.exifData);
    }


    private void populateExifData(Metadata metadata, Map<String, String> exifData) {
        ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);

        Integer[] exifTagsToExtract = new Integer[]{
                ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL,
                ExifSubIFDDirectory.TAG_EXIF_IMAGE_HEIGHT,
                ExifSubIFDDirectory.TAG_EXIF_IMAGE_WIDTH,
                ExifSubIFDDirectory.TAG_NEW_SUBFILE_TYPE,
//                ExifSubIFDDirectory.TAG_X_RESOLUTION,
//                ExifSubIFDDirectory.TAG_Y_RESOLUTION,
        };

        Arrays.asList(exifTagsToExtract).stream().forEach(
                tagIndex -> exifData.put(directory.getTagName(tagIndex),directory.getString(tagIndex)));
    }

    private void populateTags(Metadata metadata, Set<Tag> tags) {
        Lists.newArrayList(metadata.getDirectories())
                .stream()
                .flatMap(directory -> directory.getTags().stream())
                .forEach(tag -> tags.add(tag));
    }

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
    public String toString(){
        ObjectMapper objectMapper = new ObjectMapper();

        String valueAsString = StringUtils.EMPTY;
        try {
            valueAsString = objectMapper.writeValueAsString(this.exifData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return valueAsString;
    }

    public String allTagsInfo() {
        ObjectMapper objectMapper = new ObjectMapper();

        String valueAsString = StringUtils.EMPTY;
        try {
            valueAsString = objectMapper.writeValueAsString(tags);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return valueAsString;
    }

}
