package de.immomio.importer.server.data;

import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.importer.server.ftplet.ImportServerUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Johannes Hiemer, Maik Kingma
 */

@Slf4j
@Service
public class FtpAccessCustomerResolver {

    private final RestTemplate restTemplate;

    @Value("${api.url}")
    private String apiUrl;

    @Value("${api.accesscustomer-path}")
    private String accessCustomerPath;

    @Value("${api.importlog.path}")
    private String importLogPath;

    @Value("${api.importlog.incrementLogSize}")
    private String incrementLogSize;

    @Value("${api.importlog.reportError}")
    private String reportError;

    @Value("${api.import-path}")
    private String importPath;

    @Autowired
    public FtpAccessCustomerResolver(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.immomio.importer.server.data.FtpAccessCustomerResolvers#loadCustomer(
     * java.lang.String, org.apache.ftpserver.ftplet.User)
     */
    public EntityModel<LandlordCustomer> loadCustomer(String action, ImportServerUser user) {
        if (user != null) {
            Map<String, String> params = new HashMap<>();
            params.put("id", user.getId());
            ResponseEntity<EntityModel<LandlordCustomer>> customerEntity = restTemplate.exchange(
                    apiUrl + accessCustomerPath, HttpMethod.GET, null,
                    new ParameterizedTypeReference<EntityModel<LandlordCustomer>>() {
                    }, params);
            EntityModel<LandlordCustomer> customer = customerEntity.getBody();

            if (customer != null && customer.getContent() != null) {
                log.info(action + customer.getContent().getName());
            }

            return customer;
        }
        return null;
    }
}
