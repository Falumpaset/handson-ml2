package de.immomio.landlord.listener.notecomment;

import de.immomio.data.shared.entity.note.Note;
import de.immomio.data.shared.entity.note.NoteComment;
import de.immomio.landlord.service.reporting.indexing.delegate.LandlordPropertySearcherIndexingDelegate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

/**
 * @author Niklas Lindemann
 */

@Slf4j
@Component
@RepositoryEventHandler
public class LandlordNoteListener {

    private LandlordPropertySearcherIndexingDelegate propertySearcherIndexingDelegate;

    @Autowired
    public LandlordNoteListener(LandlordPropertySearcherIndexingDelegate propertySearcherIndexingDelegate) {
        this.propertySearcherIndexingDelegate = propertySearcherIndexingDelegate;
    }

    @HandleAfterSave
    public void indexNodeForReporting(Note note) {
        propertySearcherIndexingDelegate.ratingChanged(note);
    }

    @HandleAfterCreate
    public void indexCommentForReporting(NoteComment noteComment) {
        propertySearcherIndexingDelegate.commentCreated(noteComment);
    }
}
