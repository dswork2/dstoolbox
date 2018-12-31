package com.ds.toolbox.fileGroup.config;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class FileDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long fileId;

    int groupId;

    String fileName;
    String path;
    boolean imageFileIndicator;
    long fileSizeInBytes;

    @Transient
    ImageMetadata metadata;
}
