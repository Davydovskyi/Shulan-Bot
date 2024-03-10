package edu.jcourse.restservice.controller;

import edu.jcourse.jpa.entity.AppDocument;
import edu.jcourse.jpa.entity.AppPhoto;
import edu.jcourse.restservice.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RequestMapping("/files")
@RestController
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @GetMapping("/get-doc/{id}")
    public ResponseEntity<FileSystemResource> getDoc(@PathVariable String id) {
        //TODO для формирования badRequest добавить ControllerAdvice
        Optional<AppDocument> doc = fileService.getDocument(id);
        if (doc.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        AppDocument appDocument = doc.get();
        Optional<FileSystemResource> fileSystemResource = fileService.getFileSystemResource(appDocument.getBinaryContent());
        return fileSystemResource
                .map(systemResource ->
                        ResponseEntity.ok()
                                .contentType(MediaType.parseMediaType(appDocument.getMimeType()))
                                .header("Content-disposition", "attachment; filename=" + appDocument.getDocName())
                                .body(systemResource))
                .orElseGet(() -> ResponseEntity.internalServerError().build());
    }

    @GetMapping("/get-photo/{id}")
    public ResponseEntity<FileSystemResource> getPhoto(@PathVariable String id) {
        //TODO для формирования badRequest добавить ControllerAdvice
        Optional<AppPhoto> photo = fileService.getPhoto(id);
        if (photo.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Optional<FileSystemResource> fileSystemResource = fileService.getFileSystemResource(photo.get().getBinaryContent());
        return fileSystemResource
                .map(systemResource ->
                        ResponseEntity.ok()
                                .contentType(MediaType.IMAGE_JPEG)
                                .header("Content-disposition", "attachment;")
                                .body(systemResource))
                .orElseGet(() -> ResponseEntity.internalServerError().build());
    }
}