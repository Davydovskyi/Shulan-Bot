package edu.jcourse.node.service;

import edu.jcourse.jpa.entity.AppDocument;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface FileService {
    AppDocument processDoc(Message externalMessage);
}