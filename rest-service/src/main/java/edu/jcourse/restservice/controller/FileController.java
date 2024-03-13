package edu.jcourse.restservice.controller;

import edu.jcourse.jpa.entity.AppDocument;
import edu.jcourse.jpa.entity.AppPhoto;
import edu.jcourse.jpa.entity.BinaryContent;
import edu.jcourse.restservice.service.FileService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequestMapping("/files")
@RestController
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    // this changes need because temp files - they can be deleted only after server restart, so it may cause problems
    @GetMapping("/get-doc/{id}")
    public void getDoc(@PathVariable String id,
                       HttpServletResponse response) {
        //TODO для формирования badRequest добавить ControllerAdvice
        Optional<AppDocument> doc = fileService.getDocument(id);
        if (doc.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        AppDocument appDocument = doc.get();
        response.setContentType(MediaType.parseMediaType(appDocument.getMimeType()).toString());
        response.setHeader("Content-disposition", "attachment; filename=" + appDocument.getDocName());
        response.setStatus(HttpServletResponse.SC_OK);

        writeContent(response, appDocument.getBinaryContent());
    }

    @GetMapping("/get-photo/{id}")
    public void getPhoto(@PathVariable String id,
                         HttpServletResponse response) {
        //TODO для формирования badRequest добавить ControllerAdvice
        Optional<AppPhoto> photo = fileService.getPhoto(id);
        if (photo.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        response.setContentType(MediaType.IMAGE_JPEG.toString());
        response.setHeader("Content-disposition", "attachment;");
        response.setStatus(HttpServletResponse.SC_OK);

        writeContent(response, photo.get().getBinaryContent());
    }

    private void writeContent(HttpServletResponse response, BinaryContent content) {
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            outputStream.write(content.getContent());
        } catch (IOException e) {
            log.error("Error writing file", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}