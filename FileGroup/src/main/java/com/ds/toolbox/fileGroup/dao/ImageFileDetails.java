package com.ds.toolbox.fileGroup.dao;

import com.drew.imaging.FileType;
import com.drew.metadata.Tag;
import com.google.common.collect.Sets;
import javassist.NotFoundException;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@NoArgsConstructor
@Data
public class ImageFileDetails extends FileDetails {
    public ImageFileDetails(FileType fileType, String imageHeight, String imageWidth) {
        this.fileType = fileType;
        this.imageHeight = imageHeight;
        this.imageWidth = imageWidth;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long fileId;

    private FileType fileType;
    private String imageHeight;
    private String imageWidth;

    @Transient
    Set<Tag> tags;

    @Transient
    public Set<Tag> addTag(Tag tag) {
        if (tags == null) {
            tags = Sets.newHashSet();
        }
        tags.add(tag);
        return tags;
    }

    @Transient
    public Tag getTag(int tagType) throws NotFoundException {
        return tags.stream()
                .filter(tag -> tag.getTagType() == tagType)
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Tag type %d not found", tagType)));
    }
}
