package de.immomio.landlord.service.reporting.indexing;

import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.landlord.entity.user.reporting.enums.PropertySearcherEventType;
import de.immomio.data.landlord.entity.user.reporting.enums.ProposalEventType;
import de.immomio.data.landlord.entity.user.reporting.enums.RatingEventType;
import de.immomio.data.shared.entity.note.Note;
import de.immomio.data.shared.entity.note.NoteComment;
import de.immomio.data.shared.entity.property.propertyProposal.PropertyProposal;
import de.immomio.reporting.model.beans.ReportingEditorBean;
import de.immomio.reporting.model.beans.ReportingPropertyBean;
import de.immomio.reporting.model.beans.propertysearcher.ReportingCommentBean;
import de.immomio.reporting.model.event.customer.PropertySearcherEvent;
import de.immomio.reporting.model.event.customer.ProposalEvent;
import de.immomio.reporting.model.event.customer.RatingEvent;
import de.immomio.reporting.service.sender.AbstractElasticsearchCustomerIndexingSenderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author Niklas Lindemann
 */

@Slf4j
@Service
public class LandlordPropertySearcherIndexingSenderService extends AbstractElasticsearchCustomerIndexingSenderService {

    @Autowired
    public LandlordPropertySearcherIndexingSenderService(RabbitTemplate template) {
        super(template);
    }

    @Async
    public void commentCreated(NoteComment noteComment, LandlordUser user) {
        ReportingCommentBean commentBean = getReportingCommentBean(noteComment);
        PropertySearcherEvent event = createPropertySearcherEvent(
                noteComment.getNote().getUserProfile(),
                new ReportingEditorBean(user),
                PropertySearcherEventType.COMMENT_CREATED,
                commentBean);
        processPropertySearcherIndexing(user.getCustomer(), event);
    }

    @Async
    public void ratingChanged(Note note, LandlordUser user) {
        RatingEvent event = createRatingEvent(
                note.getUserProfile(),
                new ReportingEditorBean(user),
                RatingEventType.RATING_CHANGED,
                note.getRating());
        processRatingIndexing(user.getCustomer(), event);
    }

    @Async
    public void proposalOffered(PropertyProposal propertyProposal, LandlordUser user) {
        ProposalEvent event = createProposalEvent(
                propertyProposal,
                new ReportingEditorBean(user),
                ProposalEventType.PROPOSAL_OFFERED);
        processProposalIndexing(user.getCustomer(), event);
    }

    private ReportingCommentBean getReportingCommentBean(NoteComment noteComment) {
        return new ReportingCommentBean(
                noteComment.getComment(),
                new ReportingPropertyBean(noteComment.getProperty())
        );
    }
}
