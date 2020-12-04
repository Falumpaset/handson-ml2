package de.immomio.importer.server.ftplet;

import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.importer.server.handler.FileDispatcher;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.springframework.hateoas.EntityModel;

import java.util.List;

@Setter
@Getter
@Slf4j
public class ImportServerUser extends BaseUser {

    private FileDispatcher fileDispatcher;
    private int sessionCount;
    private String id;
    private UserType type;
    private EntityModel<LandlordCustomer> customer;

    public ImportServerUser(UserType type, String id, boolean enabled, List<Authority> authorities, String home) {
        this.type = type;
        this.id = id;
        super.setName(id + "-" + type.toString().toLowerCase());
        super.setEnabled(enabled);
        super.setAuthorities(authorities);
        super.setHomeDirectory(home);
    }
}
