package com.ds.toolbox.fileGroup.dao;

import lombok.Data;

@Data
public abstract class FileDetails {
    int groupId;
    String fileName;
    String filePath;
    long fileSizeInBytes;
}
