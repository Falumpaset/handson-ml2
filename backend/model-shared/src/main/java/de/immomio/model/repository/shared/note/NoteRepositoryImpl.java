package de.immomio.model.repository.shared.note;

import de.immomio.data.shared.entity.note.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;


public class NoteRepositoryImpl implements NoteRepositoryCustom {

    private final EntityManager entityManager;

    @Autowired
    public NoteRepositoryImpl(JpaContext context) {
        this.entityManager = context.getEntityManagerByManagedType(Note.class);
    }

    @Override
    @Transactional
    public void customSave(Note note) {
        if (note.isNew()) {
            entityManager.persist(note);
        } else {
            entityManager.merge(note);
        }
    }

    @Override
    public Note customFindOne(Long id) {
        return entityManager.find(Note.class, id);
    }

    @Override
    @Transactional
    public void customCreateNote(Long customerId, Long psUserId, Double rating) {
        Query query = entityManager.createNativeQuery("INSERT INTO shared.note (" +
                                                              "id, customer_id, user_profile_id, rating, created, updated) " +
                                                              "VALUES (nextval('dictionary_seq'), ?, ?, ?, NOW(), " +
                                                              "NOW())");
        query.setParameter(1, customerId);
        query.setParameter(2, psUserId);
        query.setParameter(3, rating);
        query.executeUpdate();
    }


}
