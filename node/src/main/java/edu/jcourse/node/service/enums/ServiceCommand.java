package edu.jcourse.node.service.enums;

import lombok.Getter;

@Getter
public enum ServiceCommand {
    HELP("/help"),
    REGISTRATION("/registration"),
    CANCEL("/cancel"),
    START("/start");
    private final String cmd;

    ServiceCommand(String cmd) {
        this.cmd = cmd;
    }
}