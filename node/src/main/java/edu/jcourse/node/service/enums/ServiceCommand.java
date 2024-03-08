package edu.jcourse.node.service.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

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

    public static Optional<ServiceCommand> find(String cmd) {
        return Arrays.stream(values())
                .filter(serviceCommand -> serviceCommand.getCmd().equals(cmd))
                .findFirst();
    }
}