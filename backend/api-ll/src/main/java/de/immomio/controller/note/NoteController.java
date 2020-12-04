package de.immomio.controller.note;

import de.immomio.constants.exceptions.NotAuthorizedException;
import de.immomio.data.shared.bean.note.NoteBean;
import de.immomio.data.shared.bean.note.NoteCreateBean;
import de.immomio.landlord.service.note.NoteService;
import de.immomio.landlord.service.security.UserSecurityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Maik Kingma
 */

@Slf4j
@RepositoryRestController
@RequestMapping(value = "/notes")
public class NoteController {

    private final NoteService noteService;

    private final UserSecurityService userSecurityService;

    @Autowired
    public NoteController(NoteService noteService, UserSecurityService userSecurityService) {
        this.noteService = noteService;
        this.userSecurityService = userSecurityService;
    }

    @PostMapping
    public ResponseEntity<NoteBean> createOrUpdate(@RequestBody NoteCreateBean noteCreateBean) throws NotAuthorizedException {
        NoteBean noteBean = noteService.createOrUpdateNote(noteCreateBean);
        return ResponseEntity.ok(noteBean);
    }

    @GetMapping("/search/findByUserProfile")
    public ResponseEntity<NoteBean> findByUserProfile(@RequestParam("userProfileId") Long userProfileId) {
        NoteBean noteBean = noteService.findByPSUserProfileIdAndCustomer(userProfileId, userSecurityService.getPrincipalUser().getCustomer());
        return ResponseEntity.ok(noteBean);
    }

}
