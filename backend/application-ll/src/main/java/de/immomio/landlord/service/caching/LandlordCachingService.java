package de.immomio.landlord.service.caching;

import de.immomio.data.base.entity.security.BaseAuthority;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.landlord.entity.user.LandlordUsertype;
import de.immomio.data.landlord.entity.user.right.usertyperight.LandlordUsertypeRight;
import de.immomio.model.repository.landlord.customer.user.LandlordUserRepository;
import de.immomio.model.repository.landlord.customer.user.right.usertyperight.LandlordUserTypeRightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Niklas Lindemann
 */

@Service
public class LandlordCachingService {

    private static final String AUTHORITIES_CACHE_KEY = "authorities";

    private final CacheManager cacheManager;

    private final LandlordUserTypeRightRepository landlordUserTypeRightRepository;
    private final LandlordUserRepository landlordUserRepository;

    @Autowired
    public LandlordCachingService(
            CacheManager cacheManager,
            LandlordUserTypeRightRepository landlordUserTypeRightRepository,
            LandlordUserRepository landlordUserRepository
    ) {
        this.cacheManager = cacheManager;
        this.landlordUserTypeRightRepository = landlordUserTypeRightRepository;
        this.landlordUserRepository = landlordUserRepository;
    }

    @Cacheable(value = AUTHORITIES_CACHE_KEY, key = "#userId.toString()", cacheManager = "cacheManager")
    public Collection<BaseAuthority> getAuthorities(Long userId) {
        LandlordUser user = landlordUserRepository.loadById(userId);
        LandlordUsertype userType = user.getUsertype();
        Set<BaseAuthority> authorities = new HashSet<>(user.getCustomer().getAuthorities(userType));
        List<LandlordUsertypeRight> userTypeRights = landlordUserTypeRightRepository.findAllByUserType(userType);
        if (userTypeRights == null) {
            return authorities;
        }

        for (LandlordUsertypeRight userTypeRight : userTypeRights) {
            authorities.add(new BaseAuthority(userTypeRight.getRight().getRight().getShortCode()));
        }

        return authorities;
    }
}
