package com.znyar.file;

import com.mongodb.client.gridfs.model.GridFSFile;
import com.znyar.exception.FileNotFoundException;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FileService {

    private final GridFsTemplate gridFsTemplate;
    private final FileMetadataRepository fileMetadataRepository;
    private final FileMetadataMapper fileMetadataMapper;

    public FileMetadataResponse uploadFile(MultipartFile file) throws IOException {
        ObjectId fileId = gridFsTemplate.store(file.getInputStream(), file.getOriginalFilename(), file.getContentType());
        FileMetadata fileMetadata = FileMetadata.builder()
                .fileName(file.getOriginalFilename())
                .contentType(file.getContentType())
                .size(file.getSize())
                .id(fileId.toString())
                .uploadDate(LocalDateTime.now())
                .build();
        fileMetadataRepository.save(fileMetadata);
        return fileMetadataMapper.toResponse(fileMetadata);
    }

    public FileMetadataResponse getFileMetadata(String fileId) {
        return fileMetadataRepository.findById(fileId)
                .map(fileMetadataMapper::toResponse)
                .orElseThrow(() -> new FileNotFoundException("File not found with id: " + fileId)
        );
    }

    public ResponseEntity<InputStreamResource> downloadFile(String fileId) throws IOException {
        GridFSFile file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(fileId)));
        if (file == null) {
            throw new FileNotFoundException("File not found with id: " + fileId);
        }
        InputStream inputStream = gridFsTemplate.getResource(file).getInputStream();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getMetadata().get("_contentType").toString()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(new InputStreamResource(inputStream));
    }

}
