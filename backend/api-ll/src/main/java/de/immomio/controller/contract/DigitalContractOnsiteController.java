package de.immomio.controller.contract;

import de.immomio.beans.shared.contract.tenant.DigitalContractTenantWorkflowBean;
import de.immomio.data.shared.bean.contract.DigitalContractAesCodeBean;
import de.immomio.data.shared.bean.contract.DigitalContractRedirectUrlBean;
import de.immomio.landlord.service.contract.onsite.DigitalContractOnsiteSignerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

/**
 * @author Andreas Hansen
 */
@Slf4j
@RepositoryRestController
@RequestMapping(value = "dmv/onsite")
public class DigitalContractOnsiteController {

    private final DigitalContractOnsiteSignerService onsiteSignerService;

    @Autowired
    public DigitalContractOnsiteController(DigitalContractOnsiteSignerService onsiteSignerService) {
        this.onsiteSignerService = onsiteSignerService;
    }

    @PostMapping("/confirmSignerData")
    public ResponseEntity<?> confirmSignerData(@RequestParam("internalSignerId") UUID internalSignerId) {
        onsiteSignerService.confirmSignerData(internalSignerId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/startSigning")
    public ResponseEntity<DigitalContractTenantWorkflowBean> startOnsiteSigning(@RequestParam("internalSignerId") UUID internalSignerId, @RequestBody DigitalContractRedirectUrlBean redirectUrlBean) {
        DigitalContractTenantWorkflowBean embeddedSendingBean = onsiteSignerService.startOnsiteSigning(internalSignerId, redirectUrlBean.getRedirectUrl());
        return ResponseEntity.ok(embeddedSendingBean);
    }

    @GetMapping("/aesCode")
    public ResponseEntity<DigitalContractAesCodeBean> getAesCode(@RequestParam("internalSignerId") UUID internalSignerId) {
        return ResponseEntity.ok(onsiteSignerService.getAesCode(internalSignerId));
    }

    @GetMapping("/triggerUpdateContractStatus")
    public ResponseEntity triggerUpdateContractStatus(@RequestParam("internalContractId") UUID internalContractId) {
        onsiteSignerService.triggerUpdateContractStatus(internalContractId);

        return ResponseEntity.ok().build();
    }

}
