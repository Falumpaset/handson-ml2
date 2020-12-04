package de.immomio.service.landlord.user;

import de.immomio.data.base.entity.customer.user.AbstractUser;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.landlord.entity.user.LandlordUsertype;
import de.immomio.model.repository.service.landlord.customer.user.LandlordUserRepository;
import de.immomio.service.landlord.AbstractLandlordUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Niklas Lindemann
 */

@Service
public class LandlordUserService extends AbstractLandlordUserService {

    private LandlordUserRepository landlordUserRepository;

    @Autowired
    public LandlordUserService(LandlordUserRepository landlordUserRepository) {
        this.landlordUserRepository = landlordUserRepository;
    }


    public List<String> getUsers(LandlordUsertype usertype) {
        List<LandlordUser> users = landlordUserRepository.findByUsertype(usertype);
        return users.stream().filter(user -> user.getCustomer().getActiveProduct() != null).map(AbstractUser::getEmail).collect(Collectors.toList());
    }

    public Page<LandlordUser> searchUsers(String searchTerm, Pageable pageable) {
        searchTerm = searchTerm.toLowerCase();
        searchTerm = "%" + searchTerm + "%";
        return landlordUserRepository.search(searchTerm, pageable);
    }

}
