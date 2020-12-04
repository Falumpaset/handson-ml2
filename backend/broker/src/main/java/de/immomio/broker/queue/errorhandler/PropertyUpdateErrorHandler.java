package de.immomio.broker.queue.errorhandler;

import de.immomio.messages.InternalCommunicationErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * @author Niklas Lindemann
 */

@Component
@Slf4j
public class PropertyUpdateErrorHandler extends InternalCommunicationErrorHandler {

    @Override
    public void handleError(Throwable throwable) {
        log.error("Exception thrown with: {}", exceptionAsString(throwable));
    }
}
