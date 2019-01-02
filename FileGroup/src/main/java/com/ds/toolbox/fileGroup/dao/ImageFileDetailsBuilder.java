package com.ds.toolbox.fileGroup.dao;

import com.drew.imaging.FileType;
import com.drew.metadata.Tag;
import com.ds.toolbox.fileGroup.utils.FileInfoUtils;
import com.ds.toolbox.fileGroup.utils.ImageMetadataExtractorUtil;

import java.io.File;
import java.util.Set;

public class ImageFileDetailsBuilder {

    private int groupId;
    private String fileName;
    private String filePath;
    private long fileSizeInBytes;

    private FileType fileType;
    private String imageHeight;
    private String imageWidth;

    // Transients
    private Set<Tag> metadataTags;
    private ImageMetadataExtractorUtil metadataExtractorUtil;

    public ImageFileDetailsBuilder filePath(String filePath){
        this.filePath = filePath;
        this.metadataExtractorUtil = new ImageMetadataExtractorUtil(new File(this.filePath));

        this.fileName = new File(filePath).getName();
        this.fileSizeInBytes = FileInfoUtils.getSize.apply(filePath);

        this.fileType = metadataExtractorUtil.getFileType();
        this.metadataTags = metadataExtractorUtil.getTags();
        this.imageHeight=metadataExtractorUtil.getImageHeight();
        this.imageWidth=metadataExtractorUtil.getImageWidth();
        this.fileSizeInBytes = FileInfoUtils.getSize.apply(filePath);

        return this;
    }

    public ImageFileDetails build() {
        ImageFileDetails imageFileDetails = new ImageFileDetails(fileType, imageHeight, imageWidth);
        imageFileDetails.setTags(metadataTags);
        imageFileDetails.setFileName(this.fileName);
        imageFileDetails.setFilePath(this.filePath);
        imageFileDetails.setFileSizeInBytes(this.fileSizeInBytes);
        return imageFileDetails;
    }


}