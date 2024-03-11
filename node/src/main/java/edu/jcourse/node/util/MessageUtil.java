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
    public static final String DOC_IS_UPLOADED_MESSAGE = "Document is uploaded. Link to download: %s";
    public static final String PHOTO_IS_UPLOADED_MESSAGE = "Photo is uploaded. Link to download: %s";
    public static final String USER_NOT_ACTIVE_MESSAGE = "Please, register or activate your account to download content.";
    public static final String CANCEL_CURRENT_COMMAND_MESSAGE = "Cancel the current command by typing /cancel to upload the files.";
    public static final String NOT_IMPLEMENTED_YET_MESSAGE = "Not implemented yet!";
    public static final String UPLOAD_FILE_ERROR_MESSAGE = "Error while uploading file! Please try again";
    public static final String UPLOAD_PHOTO_ERROR_MESSAGE = "Error while uploading photo! Please try again";
    public static final String UPLOAD_ERROR_MESSAGE = "Bad response from telegram service: ";
}