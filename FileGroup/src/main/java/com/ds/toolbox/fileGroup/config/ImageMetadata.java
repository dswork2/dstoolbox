package com.ds.toolbox.fileGroup.config;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.function.Function;

public class ImageMetadata {

    ImageMetadata(File file) {
        super();
        this.metadata = metadataExtractor.apply(file);
        this.tags = Sets.newConcurrentHashSet();
        Lists.newArrayList(this.metadata.getDirectories())
                .stream()
                .flatMap(directory -> directory.getTags().stream())
                .forEach(tag -> this.tags.add(tag));
    }

    Metadata metadata;
    Set<Tag> tags;

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

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
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
