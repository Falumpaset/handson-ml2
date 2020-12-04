package de.immomio.controller.impl;

import de.immomio.controller.base.BaseExternalContractController;
import de.immomio.service.impl.contract.DigitalContractServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Niklas Lindemann
 */

@Tag(name = "Contract")
@RequestMapping("/api/contract")
@RestController
public class ExternalContractController extends BaseExternalContractController<DigitalContractServiceImpl> {

    public ExternalContractController(DigitalContractServiceImpl digitalContractService) {
        super(digitalContractService);
    }

}
