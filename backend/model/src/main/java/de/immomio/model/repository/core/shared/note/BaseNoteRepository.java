package de.immomio.model.repository.core.shared.note;

import de.immomio.data.shared.entity.note.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseNoteRepository extends JpaRepository<Note, Long> {



}
