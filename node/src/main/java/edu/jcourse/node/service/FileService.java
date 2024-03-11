package edu.jcourse.node.service;

import edu.jcourse.jpa.entity.AppDocument;
import edu.jcourse.jpa.entity.AppPhoto;
import edu.jcourse.node.service.enums.LinkType;
import edu.jcourse.util.CryptoUtil;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface FileService {
    AppDocument processDoc(Message telegramMessage);

    AppPhoto processPhoto(Message telegramMessage);

    String generateLink(Long id, LinkType linkType, CryptoUtil cryptoUtil);
}