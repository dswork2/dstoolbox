package com.ds.toolbox.fileGroup.config;

import com.drew.metadata.Metadata;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.imageio.metadata.IIOMetadata;
import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class FileDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long fileId;

    int groupId;

    String fileName;
    String path;
    boolean isImageFile;

    long fileSizeInBytes;

    @Transient
    ImageMetadata metadata;

}
