package edu.jcourse.node.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MessageUtil {
    public static final String UNKNOWN_ERROR_MESSAGE = "Unknown error! Enter /cancel and Please try again!";
    public static final String CANCEL_PROCESS_MESSAGE = "Command canceled!";
    public static final String HELP_MESSAGE = """
            List of available commands:
            /registration - user registration;
            /cancel - cancel current command;
            """;
    public static final String START_MESSAGE = "Hello! To get a list of available commands, enter /help";
    public static final String UNKNOWN_COMMAND_MESSAGE = "Unknown command! To get a list of available commands, enter /help";
    public static final String DOC_IS_DOWNLOADED_MESSAGE = "Document is downloaded. Link to download: http://test.ru/get-doc/777";
    public static final String PHOTO_IS_DOWNLOADED_MESSAGE = "Photo is downloaded. Link to download: http://test.ru/get-photo/777";
    public static final String USER_NOT_ACTIVE_MESSAGE = "Please, register or activate your account to download content.";
    public static final String CANCEL_CURRENT_COMMAND_MESSAGE = "Cancel the current command by typing /cancel to upload the files.";
}