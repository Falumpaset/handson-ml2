package de.immomio.model.repository.shared.note;

import de.immomio.data.shared.entity.note.Note;
import de.immomio.model.repository.core.shared.note.BaseNoteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Optional;


public interface NoteRepository extends BaseNoteRepository, NoteRepositoryCustom {

    @Override
    @RestResource(exported = false)
    Page<Note> findAll(Pageable pageable);

    @Override
    @RestResource(exported = false)
    Optional<Note> findById(Long id);

    @RestResource(exported = false)
    @Query("SELECT o FROM #{#entityName} o " +
                   "WHERE o.customer.id = :customerId " +
                   "AND o.userProfile.id = :userProfileId ")
    Optional<Note> findByPSUserProfileIdAndCustomerId(@Param("userProfileId") Long userProfileId,
                                 @Param("customerId") Long customerId);

    @Override
    @RestResource(exported = false)
    Note save(@Param("note") Note note);

    @Override
    @RestResource(exported = false)
    @PreAuthorize("#note.customer.id == principal?.id")
    void delete(@Param("note") Note note);

}
