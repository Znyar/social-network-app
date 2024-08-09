package com.znyar.file;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class FileMetadataResponse {

    private String id;
    private String fileName;
    private String contentType;
    private long size;
    private LocalDateTime uploadDate;

}
