package de.immomio.model.repository.shared.note;

import de.immomio.data.shared.entity.note.Note;
import de.immomio.data.shared.entity.note.NoteComment;
import de.immomio.model.repository.core.shared.note.BaseNoteCommentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;


public interface NoteCommentRepository extends BaseNoteCommentRepository {

    @Override
    @RestResource(exported = false)
    Page<NoteComment> findAll(Pageable pageable);

    @Override
    @PostAuthorize("returnObject?.get()?.note?.customer?.id == principal?.customer?.id ")
    Optional<NoteComment> findById(Long id);

    @Override
    @RestResource(exported = false)
    NoteComment save(@Param(COMMENT_PARAM) NoteComment comment);

    @Override
    @PreAuthorize("#comment.user.customer.id == principal?.customer.id")
    void delete(@Param(COMMENT_PARAM) NoteComment comment);

    @PreAuthorize("#note.customer.id == principal?.customer.id")
    Page<NoteComment> findByNote(@Param("note") Note note, Pageable pageable);

    @RestResource(exported = false)
    List<NoteComment> findByNote(@Param("note") Note note);
}
