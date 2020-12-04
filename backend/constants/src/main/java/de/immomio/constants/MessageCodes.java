package de.immomio.constants;

public abstract class MessageCodes {

    public static final String CHANGEEMAIL_CATEGORY = "00a";

    public static final String OK_MESSAGE = "0";

    public static final String ERROR_MESSAGE = "1";

    public static final String CHANGEEMAIL_OK = CHANGEEMAIL_CATEGORY + "001" + OK_MESSAGE;

    public static final String CHANGEEMAIL_ERROR = CHANGEEMAIL_CATEGORY + "001" + ERROR_MESSAGE;

    private MessageCodes() {

    }
}
