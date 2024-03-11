package edu.jcourse.node.service.enums;

import lombok.Getter;

@Getter
public enum LinkType {
    GET_DOC("files/get-doc/"),
    GET_PHOTO("files/get-photo/");

    private final String link;

    LinkType(String link) {
        this.link = link;
    }
}