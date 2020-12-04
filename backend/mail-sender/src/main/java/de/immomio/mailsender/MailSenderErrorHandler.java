/**
 *
 */
package de.immomio.mailsender;

import de.immomio.messages.InternalCommunicationErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author Johannes Hiemer.
 */
@Component
public class MailSenderErrorHandler extends InternalCommunicationErrorHandler {

    protected static final Logger LOG = LoggerFactory.getLogger(MailSenderErrorHandler.class);

    //	@Autowired
    //	private BrokerExceptionLogRepository brokerExceptionLogRepository;

    //	@Autowired
    //	private UsageRepository usageRepository;

    //	/* (non-Javadoc)
    //	 * @see org.springframework.util.ErrorHandler#handleError(java.lang.Throwable)
    //	 */InternalCommunicationErrorHandler.java
    //	public void handleError(FlatApplication flatApplication, Throwable t) {
    //		String exceptionAsString = exceptionAsString(t);
    //
    //		brokerExceptionLogRepository.save(new BrokerExceptionLog(flatApplication, exceptionAsString));
    //		log.error("Exception thrown with: {}", exceptionAsString);
    //	}

    //	/* (non-Javadoc)
    //	 * @see org.springframework.util.ErrorHandler#handleError(java.lang.Throwable)
    //	 */
    //	public void handleError(Flat flat, Usage usage, Throwable t) {
    //		String exceptionAsString = exceptionAsString(t);
    //
    //		brokerExceptionLogRepository.save(new BrokerExceptionLog(flat, exceptionAsString));
    //
    //		StringBuilder desc = new StringBuilder();
    //		desc.append("Exception thrown with: ");
    //		desc.append("[flat (");
    //		desc.append(flat == null ? "null" : flat.getId().toString());
    //		desc.append(") usage (");
    //		desc.append(usage == null ? "null" : usage.getId());
    //		desc.append(")]");
    //
    //		log.error(desc.toString(), t);
    //
    //		if(usage != null)
    //			try {
    //				usageRepository.customDelete(usage);
    //			} catch (Exception e) {
    //				log.error(desc.toString(), e);
    //			}
    //	}

    @Override
    public void handleError(Throwable t) {
        LOG.error("Exception thrown with: {}", exceptionAsString(t));
    }

}