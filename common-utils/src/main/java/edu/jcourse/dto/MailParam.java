package edu.jcourse.dto;

import lombok.Builder;

@Builder
public record MailParam(String id, String emailTo) {
}