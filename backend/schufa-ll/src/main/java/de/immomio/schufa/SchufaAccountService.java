package de.immomio.schufa;

import de.immomio.common.encryption.CryptoUtils;
import de.immomio.common.encryption.exception.CryptoException;
import de.immomio.constants.exceptions.EncryptionException;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.schufa.LandlordSchufaAccount;
import de.immomio.model.repository.landlord.schufa.LandlordSchufaAccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Niklas Lindemann
 */

@Slf4j
@Service
public class SchufaAccountService {

    private static final String ERROR_AT_ENCRYPTION = "ERROR_AT_ENCRYPTION_L";

    private LandlordSchufaAccountRepository schufaAccountRepository;

    @Value("${encryption.credentials.key}")
    private String encryptionKey;

    @Autowired
    public SchufaAccountService(LandlordSchufaAccountRepository schufaAccountRepository) {
        this.schufaAccountRepository = schufaAccountRepository;
    }

    public void createSchufaAccount(
            LandlordCustomer customer,
            String username,
            String password
    ) throws EncryptionException {
        LandlordSchufaAccount schufaAccount = schufaAccountRepository.findOne();
        if (schufaAccount == null) {
            schufaAccount = new LandlordSchufaAccount();
            schufaAccount.setCustomer(customer);
        }
        try {
            schufaAccount.setUsername(username);
            schufaAccount.setPassword(CryptoUtils.encryptString(encryptionKey, password));
            schufaAccountRepository.save(schufaAccount);
        } catch (CryptoException e) {
            log.error("error at encrypting schufa credentials", e);
            throw new EncryptionException(ERROR_AT_ENCRYPTION);
        }

    }
}
