package com.znyar.file;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("file_metadata")
@Builder
@Data
public class FileMetadata {

    @Id
    private String id;
    private String fileName;
    private String contentType;
    private long size;
    private LocalDateTime uploadDate;

}
