package de.immomio.model.repository.core.landlord.customer.messagesource;

import com.neovisionaries.i18n.LocaleCode;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.messagesource.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * @author Johannes Hiemer.
 * @author Bastian Bliemeister
 */
public interface BaseLandlordMessageSourceRepository extends JpaRepository<MessageSource, Long> {

    @Query("SELECT m FROM #{#entityName} m WHERE (m.locale = :locale AND m.messageKey = :messageKey) "
                   + "AND m.customer = :customer")
    MessageSource findByLanguageKeyAndMessageKeyAndCustomer(@Param("locale") LocaleCode locale,
                                                            @Param("messageKey") String messageKey,
                                                            @Param("customer") LandlordCustomer customer);

    @Override
    @Query("SELECT o FROM #{#entityName} o WHERE o.id = :id")
    Optional<MessageSource> findById(@Param("id") Long id);

    @Override
    @Query("SELECT o FROM #{#entityName} o")
    Page<MessageSource> findAll(Pageable pageable);

    @Override
    void deleteById(@Param("id") Long id);

    @Override
    void delete(@Param("messageSource") MessageSource messageSource);

    @Override
    <T extends MessageSource> T save(@Param("messageSource") T messageSource);

    List<MessageSource> findAllByCustomerAndLocale(LandlordCustomer customer, LocaleCode locale);
}
