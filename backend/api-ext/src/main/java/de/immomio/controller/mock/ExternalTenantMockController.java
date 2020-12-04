package de.immomio.controller.mock;

import de.immomio.controller.base.BaseExternalTenantController;
import de.immomio.service.mock.tenant.ExternalTenantInfoServiceMock;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Niklas Lindemann
 */

@RequestMapping("/api/mock/tenant")
@RestController
@Tag(name = "Tenant Info (Mock)")
public class ExternalTenantMockController extends BaseExternalTenantController<ExternalTenantInfoServiceMock> {

    @Autowired
    public ExternalTenantMockController(ExternalTenantInfoServiceMock tenantInfoService) {
        super(tenantInfoService);
    }

}
