package de.immomio.model.repository.propertysearcher.note;

import de.immomio.data.propertysearcher.entity.user.PropertySearcherUser;
import de.immomio.data.shared.entity.note.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Niklas Lindemann
 */

@Repository
public interface PropertysearcherNoteRepository extends JpaRepository<Note, Long> {

    @Transactional
    @Modifying
    @Query("update NoteComment nC set nC.note = :note where nC.note in :oldNotes")
    void switchNoteCommentNotes(@Param("note") Note note, @Param("oldNotes") List<Note> oldNotes);


    @Query("select n from Note n where n.userProfile.user = :user")
    List<Note> findAllByUser(@Param("user") PropertySearcherUser user);

}
