package edu.jcourse.node.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.jcourse.jpa.entity.AppDocument;
import edu.jcourse.jpa.entity.AppPhoto;
import edu.jcourse.jpa.entity.BinaryContent;
import edu.jcourse.jpa.repository.AppDocumentRepository;
import edu.jcourse.jpa.repository.AppPhotoRepository;
import edu.jcourse.jpa.repository.BinaryContentRepository;
import edu.jcourse.node.exception.UploadFileException;
import edu.jcourse.node.service.FileService;
import edu.jcourse.node.service.enums.LinkType;
import edu.jcourse.util.CryptoUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import static edu.jcourse.node.util.MessageUtil.UPLOAD_ERROR_MESSAGE;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final AppDocumentRepository appDocumentRepository;
    private final AppPhotoRepository appPhotoRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final ObjectMapper objectMapper;
    @Value("${bot.token}")
    private String token;
    @Value("${bot.service.file-info.uri}")
    private String fileInfoUri;
    @Value("${bot.service.file-storage.uri}")
    private String fileStorageUri;
    @Value("${app.link-for-files}")
    private String linkAddress;

    @Override
    @Transactional
    public AppDocument processDoc(Message telegramMessage) {
        String fileId = telegramMessage.getDocument().getFileId();
        ResponseEntity<String> response = getFilePath(fileId);
        if (response.getStatusCode() == HttpStatus.OK) {
            BinaryContent persistentBinaryContent = getBinaryContent(response);
            AppDocument appDocument = buildAppDocument(telegramMessage.getDocument(), persistentBinaryContent);
            return appDocumentRepository.save(appDocument);
        } else {
            throw new UploadFileException(UPLOAD_ERROR_MESSAGE + response);
        }
    }

    @Override
    @Transactional
    public AppPhoto processPhoto(Message telegramMessage) {
        PhotoSize telegramPhoto = telegramMessage.getPhoto().getLast();
        String fileId = telegramPhoto.getFileId();
        ResponseEntity<String> response = getFilePath(fileId);
        if (response.getStatusCode() == HttpStatus.OK) {
            BinaryContent persistentBinaryContent = getBinaryContent(response);
            AppPhoto transientAppPhoto = buildAppPhoto(telegramPhoto, persistentBinaryContent);
            return appPhotoRepository.save(transientAppPhoto);
        } else {
            throw new UploadFileException(UPLOAD_ERROR_MESSAGE + response);
        }
    }

    @Override
    public String generateLink(Long id, LinkType linkType, CryptoUtil cryptoUtil) {
        return linkAddress + linkType.getLink() + cryptoUtil.encrypt(id);
    }

    private AppDocument buildAppDocument(Document telegramDoc, BinaryContent persistentBinaryContent) {
        return AppDocument.builder()
                .telegramFileId(telegramDoc.getFileId())
                .docName(telegramDoc.getFileName())
                .binaryContent(persistentBinaryContent)
                .mimeType(telegramDoc.getMimeType())
                .fileSize(telegramDoc.getFileSize())
                .build();
    }

    private AppPhoto buildAppPhoto(PhotoSize telegramPhoto, BinaryContent persistentBinaryContent) {
        return AppPhoto.builder()
                .telegramFileId(telegramPhoto.getFileId())
                .binaryContent(persistentBinaryContent)
                .fileSize(telegramPhoto.getFileSize())
                .build();
    }

    private BinaryContent getBinaryContent(ResponseEntity<String> response) {
        String filePath = getFilePath(response);
        byte[] fileInByte = downloadFile(filePath);
        BinaryContent transientBinaryContent = BinaryContent.builder()
                .content(fileInByte)
                .build();
        return binaryContentRepository.save(transientBinaryContent);
    }

    @SneakyThrows
    private String getFilePath(ResponseEntity<String> response) {
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        return jsonNode.get("result").get("file_path").asText();
    }

    private ResponseEntity<String> getFilePath(String fileId) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> request = new HttpEntity<>(headers);

        return restTemplate.exchange(
                fileInfoUri,
                HttpMethod.GET,
                request,
                String.class,
                token, fileId
        );
    }

    private byte[] downloadFile(String filePath) {
        String fullUri = fileStorageUri
                .replace("{token}", token)
                .replace("{filePath}", filePath);

        //TODO подумать над оптимизацией
        try (InputStream is = URI.create(fullUri).toURL().openStream()) {
            return is.readAllBytes();
        } catch (IOException e) {
            throw new UploadFileException(fullUri, e);
        }
    }
}