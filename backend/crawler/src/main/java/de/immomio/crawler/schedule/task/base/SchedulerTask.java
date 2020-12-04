package de.immomio.crawler.schedule.task.base;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;

public interface SchedulerTask {

    boolean run() throws NoSuchProviderException, MessagingException;

}
