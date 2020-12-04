package de.immomio.controller.mock;

import de.immomio.controller.base.BaseExternalContractController;
import de.immomio.service.mock.contract.DigitalContractServiceMock;
import de.immomio.service.validation.DigitalContractValidationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Niklas Lindemann
 */

@RequestMapping("/api/mock/contract")
@RestController
@Tag(name = "Contract (Mock)")
public class ExternalContractMockController extends BaseExternalContractController<DigitalContractServiceMock> {

    public ExternalContractMockController(DigitalContractServiceMock digitalContractService, DigitalContractValidationService validationService) {
        super(digitalContractService);
    }

}
