package edu.jcourse.restservice.service;

import edu.jcourse.jpa.entity.AppDocument;
import edu.jcourse.jpa.entity.AppPhoto;
import edu.jcourse.jpa.entity.BinaryContent;
import org.springframework.core.io.FileSystemResource;

import java.util.Optional;

public interface FileService {
    Optional<AppDocument> getDocument(String id);

    Optional<AppPhoto> getPhoto(String id);

    Optional<FileSystemResource> getFileSystemResource(BinaryContent binaryContent);
}