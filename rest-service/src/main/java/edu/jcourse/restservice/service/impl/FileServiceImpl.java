package edu.jcourse.restservice.service.impl;

import edu.jcourse.jpa.entity.AppDocument;
import edu.jcourse.jpa.entity.AppPhoto;
import edu.jcourse.jpa.repository.AppDocumentRepository;
import edu.jcourse.jpa.repository.AppPhotoRepository;
import edu.jcourse.restservice.service.FileService;
import edu.jcourse.util.CryptoUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileServiceImpl implements FileService {
    private final AppDocumentRepository appDocumentRepository;
    private final AppPhotoRepository appPhotoRepository;
    private final CryptoUtil docCryptoUtil;
    private final CryptoUtil photoCryptoUtil;

    @Override
    public Optional<AppDocument> getDocument(String docId) {
        return Optional.ofNullable(docId)
                .map(docCryptoUtil::decrypt)
                .flatMap(appDocumentRepository::findById);
    }

    @Override
    public Optional<AppPhoto> getPhoto(String photoId) {
        return Optional.ofNullable(photoId)
                .map(photoCryptoUtil::decrypt)
                .flatMap(appPhotoRepository::findById);
    }
}