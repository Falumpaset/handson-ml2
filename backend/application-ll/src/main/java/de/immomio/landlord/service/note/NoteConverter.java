package de.immomio.landlord.service.note;

import de.immomio.data.landlord.bean.user.AgentInfo;
import de.immomio.data.shared.bean.note.NoteBean;
import de.immomio.data.shared.bean.note.NoteCommentBean;
import de.immomio.data.shared.entity.note.Note;
import de.immomio.data.shared.entity.note.NoteComment;
import de.immomio.service.landlord.property.PropertyConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Niklas Lindemann
 */
@Service
public class NoteConverter {

    private PropertyConverter propertyConverter;

    @Autowired
    public NoteConverter(PropertyConverter propertyConverter) {
        this.propertyConverter = propertyConverter;
    }

    public NoteBean convertNoteToNoteBean(Note note, List<NoteComment> comments) {
        return NoteBean.builder()
                .id(note.getId())
                .rating(note.getRating())
                .comments(comments
                        .stream()
                        .map(this::convertNoteCommentToNoteCommentBean)
                        .sorted(Comparator.comparing(NoteCommentBean::getCreated))
                        .collect(Collectors.toList())).build();
    }

    public NoteCommentBean convertNoteCommentToNoteCommentBean(NoteComment noteComment) {
        NoteCommentBean.NoteCommentBeanBuilder noteCommentBeanBuilder = NoteCommentBean.builder()
                .comment(noteComment.getComment())
                .id(noteComment.getId())
                .agentInfo(new AgentInfo(noteComment.getUser()))
                .created(noteComment.getCreated());
        if (noteComment.getProperty() != null) {
            noteCommentBeanBuilder.property(propertyConverter.convertToPropertyBean(noteComment.getProperty()));
        }
        return noteCommentBeanBuilder.build();
    }
}
