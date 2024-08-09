package com.znyar.file;

import org.springframework.stereotype.Component;

@Component
public class FileMetadataMapper {

    public FileMetadataResponse toResponse(FileMetadata fileMetadata) {
        return FileMetadataResponse.builder()
                .id(fileMetadata.getId())
                .contentType(fileMetadata.getContentType())
                .fileName(fileMetadata.getFileName())
                .size(fileMetadata.getSize())
                .uploadDate(fileMetadata.getUploadDate())
                .build();
    }

}
