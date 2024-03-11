package edu.jcourse.mail.dto;

import lombok.Builder;

@Builder
public record MailParam(String id, String emailTo) {
}