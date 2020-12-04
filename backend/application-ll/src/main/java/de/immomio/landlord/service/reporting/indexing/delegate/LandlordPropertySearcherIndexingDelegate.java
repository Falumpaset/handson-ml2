package de.immomio.landlord.service.reporting.indexing.delegate;

import de.immomio.data.shared.entity.note.Note;
import de.immomio.data.shared.entity.note.NoteComment;
import de.immomio.data.shared.entity.property.propertyProposal.PropertyProposal;
import de.immomio.landlord.service.reporting.indexing.LandlordPropertySearcherIndexingSenderService;
import de.immomio.landlord.service.security.UserSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Niklas Lindemann
 */

@Component
public class LandlordPropertySearcherIndexingDelegate extends BaseLandlordIndexingDelegate {

    private LandlordPropertySearcherIndexingSenderService landlordPropertySearcherIndexingService;

    @Autowired
    public LandlordPropertySearcherIndexingDelegate(
            UserSecurityService userSecurityService,
            LandlordPropertySearcherIndexingSenderService landlordPropertySearcherIndexingService
    ) {
        super(userSecurityService);
        this.landlordPropertySearcherIndexingService =  landlordPropertySearcherIndexingService;
    }

    public void commentCreated(NoteComment noteComment) {
        landlordPropertySearcherIndexingService.commentCreated(noteComment, getPrincipal());
    }

    public void ratingChanged(Note note) {
        landlordPropertySearcherIndexingService.ratingChanged(note, getPrincipal());
    }

    public void proposalOffered(PropertyProposal propertyProposal) {
        landlordPropertySearcherIndexingService.proposalOffered(propertyProposal, getPrincipal());
    }

}
