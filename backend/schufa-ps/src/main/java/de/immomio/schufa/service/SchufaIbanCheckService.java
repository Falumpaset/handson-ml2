package de.immomio.schufa.service;

import de.immomio.data.base.bean.schufa.cbi.CbiRequest;
import de.immomio.data.base.bean.schufa.cbi.SchufaAddress;
import de.immomio.data.base.bean.schufa.cbi.SchufaConnectorException;
import de.immomio.data.base.bean.schufa.cbi.accountNumberCheck.AccountNumberCheck;
import de.immomio.data.base.bean.schufa.cbi.accountNumberCheck.AccountNumberCheckResponse;
import de.immomio.data.base.bean.schufa.cbi.accountNumberCheck.information.SchufaBankAccount;
import de.immomio.data.base.bean.schufa.cbi.creditRating.enums.SchufaGenderType;
import de.immomio.data.base.bean.schufa.cbi.enums.CbiActionType;
import de.immomio.data.shared.bean.contract.DigitalContractSignerData;
import de.immomio.data.shared.entity.contract.signer.DigitalContractSigner;
import de.immomio.schufa.SchufaConfig;
import de.immomio.schufa.SchufaConnector;
import de.immomio.schufa.SchufaProdConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Niklas Lindemann
 */

@Service
public class SchufaIbanCheckService {

    private SchufaConnector schufaConnector;
    private SchufaConfig schufaConfig;
    private SchufaProdConfig schufaProdConfig;

    @Autowired
    public SchufaIbanCheckService(SchufaConnector schufaConnector, SchufaConfig schufaConfig, SchufaProdConfig schufaProdConfig) {
        this.schufaConnector = schufaConnector;
        this.schufaConfig = schufaConfig;
        this.schufaProdConfig = schufaProdConfig;
    }

    public AccountNumberCheckResponse processSchufaIbanCheck(AccountNumberCheck accountNumberCheck) throws SchufaConnectorException {
        return schufaConnector.runAccountNumberCheckJob(new Date().getTime(), accountNumberCheck);
    }

    public AccountNumberCheck getAccountNumberCheckData(DigitalContractSigner signer, String iban) {
        DigitalContractSignerData signerData = signer.getData();

        SchufaBankAccount schufaBankAccount = new SchufaBankAccount(iban);
        CbiRequest cbiRequest = new CbiRequest(CbiActionType.SCHUFA2_ANFRAGE_KONTONUMMERN_CHECK, signer.getId() + "_" + new Date().getTime());
        SchufaAddress schufaAddress = new SchufaAddress(signerData.getAddress());
        SchufaGenderType genderType = signerData.getGender() != null ? SchufaGenderType.convertGenderType(signerData.getGender()) : null;


        String participantNumber = isTestIban(iban) ? schufaConfig.getAccountCheckParticipantNumber() : schufaProdConfig.getAccountCheckParticipantNumber();
        String participantPassword = isTestIban(iban) ? schufaConfig.getAccountCheckParticipantPassword() : schufaProdConfig.getAccountCheckParticipantPassword();
        return new AccountNumberCheck(
                participantNumber,
                participantPassword,
                cbiRequest,
                schufaAddress,
                signerData.getFirstname(),
                signerData.getLastname(),
                genderType,
                signerData.getDateOfBirth(),
                schufaBankAccount,
                null,
                null);
    }

    public boolean isTestIban(String iban) {
        return schufaConfig.getTestibans().contains(iban);
    }

}
