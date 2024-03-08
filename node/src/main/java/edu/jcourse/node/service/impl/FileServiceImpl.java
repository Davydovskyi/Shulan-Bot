package edu.jcourse.node.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.jcourse.jpa.entity.AppDocument;
import edu.jcourse.jpa.entity.BinaryContent;
import edu.jcourse.jpa.repository.AppDocumentRepository;
import edu.jcourse.jpa.repository.BinaryContentRepository;
import edu.jcourse.node.exception.UploadFileException;
import edu.jcourse.node.service.FileService;
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

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final AppDocumentRepository appDocumentRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final ObjectMapper objectMapper;
    @Value("${bot.token}")
    private String token;
    @Value("${bot.service.file-info.uri}")
    private String fileInfoUri;
    @Value("${bot.service.file-storage.uri}")
    private String fileStorageUri;

    @Override
    @Transactional
    public AppDocument processDoc(Message message) {
        String fileId = message.getDocument().getFileId();
        ResponseEntity<String> response = getFilePath(fileId);
        if (response.getStatusCode() == HttpStatus.OK) {
            String filePath = getFilePath(response);
            byte[] fileInByte = downloadFile(filePath);

            BinaryContent binaryContent = BinaryContent.builder().content(fileInByte).build();
            BinaryContent persistentBinaryContent = binaryContentRepository.save(binaryContent);
            AppDocument appDocument = buildAppDocument(message.getDocument(), persistentBinaryContent);
            return appDocumentRepository.save(appDocument);
        } else {
            throw new UploadFileException("Bad response from telegram service: " + response);
        }
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