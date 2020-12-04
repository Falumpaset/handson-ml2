package de.immomio.model.repository.core.shared.note;

import de.immomio.data.shared.entity.note.NoteComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "noteComments")
public interface BaseNoteCommentRepository extends JpaRepository<NoteComment, Long> {

    String COMMENT_PARAM = "comment";

}
