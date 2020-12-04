/**
 *
 */
package de.immomio.common.ical;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.property.Status;
import biweekly.property.Summary;
import biweekly.util.Duration;
import com.google.common.io.Files;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * @author Johannes Hiemer.
 */
public class ICalEventHandler {

    public static final String ICAL_TITLE = "Besichtigungstermin";

    public static final String LANGUAGE = "de_DE";

    public static final String INVITE_ICS_FILE_NAME = "invite.ics";

    private File folder;

    public File createEvent(String invitationId, String eventTitle, String location,
                            String language, Date start, int durationInMinutes) throws IOException {
        ICalendar ical = new ICalendar();
        VEvent event = new VEvent();
        event.setSequence(1);
        event.setUid(invitationId);
        Summary summary = event.setSummary(eventTitle);
        summary.setLanguage(language);
        event.setLocation(location);
        event.setDateStart(start);

        Duration duration = new Duration
                .Builder()
                .minutes(durationInMinutes)
                .build();
        event.setDuration(duration);

        ical.addEvent(event);

        return eventToFile(Biweekly.write(ical).go(), "invitie.ics");
    }

    public File cancelEvent(String invitationId, String eventTitle) throws IOException {
        ICalendar ical = new ICalendar();
        VEvent event = new VEvent();
        event.setSequence(2);
        event.setUid(invitationId);
        event.setStatus(Status.cancelled());
        event.setSummary(eventTitle);
        ical.addEvent(event);

        return eventToFile(Biweekly.write(ical).go(), "cancelled.ics");
    }

    public File eventToFile(String event, String fileName) throws IOException {
        this.initializeFolder();

        File file = new File(folder.getAbsolutePath() + File.separator + fileName);
        FileUtils.writeStringToFile(file, event);

        return file;
    }

    private File initializeFolder() {
        this.folder = Files.createTempDir();

        return this.folder;
    }

    public void cleanUpFolder() throws IOException {
        FileUtils.deleteDirectory(this.folder);
    }

    protected File getFolder() {
        return this.folder;
    }
}
