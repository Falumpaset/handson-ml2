package de.immomio.recipient.handler;

import org.springframework.messaging.Message;

/**
 * @author Fabian Beck.
 */

public interface MessageHandler {
    void handleMessage(Message message);
}
