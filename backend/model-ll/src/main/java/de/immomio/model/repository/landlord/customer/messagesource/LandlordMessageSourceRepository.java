/**
 *
 */
package de.immomio.model.repository.landlord.customer.messagesource;

import com.neovisionaries.i18n.LocaleCode;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.messagesource.MessageSource;
import de.immomio.model.repository.core.landlord.customer.messagesource.BaseLandlordMessageSourceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Optional;

/**
 * @author Johannes Hiemer.
 * @author Bastian Bliemeister
 */
public interface LandlordMessageSourceRepository extends BaseLandlordMessageSourceRepository {

    @RestResource(exported = false)
    @Query("SELECT m FROM #{#entityName} m WHERE (m.locale = :locale AND m.messageKey = :messageKey) "
            + "AND m.customer = :customer")
    MessageSource findByLanguageKeyAndMessageKeyAndCustomer(@Param("locale") LocaleCode locale,
                                                            @Param("messageKey") String messageKey,
                                                            @Param("customer") LandlordCustomer customer);

    @Override
    @Query("SELECT o FROM #{#entityName} o WHERE o.id = :id AND "
            + "(o.customer.id = ?#{principal.customer.id})")
    Optional<MessageSource> findById(@Param("id") Long id);

    @Override
    @Query("SELECT o FROM #{#entityName} o WHERE o.customer.id = ?#{principal.customer.id}")
    Page<MessageSource> findAll(Pageable pageable);

    @Override
    @RestResource(exported = false)
    void deleteById(@Param("id") Long id);

    @Override
    @PreAuthorize("#messageSource.customer.id == principal?.customer?.id")
    void delete(@Param("messageSource") MessageSource messageSource);

    @Override
    @PreAuthorize("#messageSource.customer.id == principal?.customer?.id") <T extends MessageSource> T save(
            @Param("messageSource") T messageSource);
}
