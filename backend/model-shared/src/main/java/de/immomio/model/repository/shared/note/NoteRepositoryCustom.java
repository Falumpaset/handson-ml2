package de.immomio.model.repository.shared.note;

import de.immomio.data.shared.entity.note.Note;

/**
 * @author Maik Kingma
 */

public interface NoteRepositoryCustom {

    void customSave(Note property);

    Note customFindOne(Long id);

    void customCreateNote(Long customerId, Long psUserId, Double rating);

}