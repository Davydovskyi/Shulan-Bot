package edu.jcourse.restservice.service;

import edu.jcourse.jpa.entity.AppDocument;
import edu.jcourse.jpa.entity.AppPhoto;

import java.util.Optional;

public interface FileService {
    Optional<AppDocument> getDocument(String id);

    Optional<AppPhoto> getPhoto(String id);
}