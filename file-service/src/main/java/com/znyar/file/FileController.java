package com.znyar.file;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping
    public ResponseEntity<FileMetadataResponse> uploadFile(
            @RequestParam("file") MultipartFile file
    ) {
        try {
            return ResponseEntity.ok(fileService.uploadFile(file));
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{file-id}/metadata")
    public ResponseEntity<FileMetadataResponse> getFileMetadata(
            @PathVariable("file-id") String fileId
    ) {
        return ResponseEntity.ok(fileService.getFileMetadata(fileId));
    }

    @GetMapping("/{file-id}")
    public ResponseEntity<InputStreamResource> downloadFile(
            @PathVariable("file-id") String fileId
    ) throws IOException {
        return fileService.downloadFile(fileId);
    }

}
