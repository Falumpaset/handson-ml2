package de.immomio.service.sdtest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import de.immomio.data.landlord.bean.property.data.PropertyData;
import de.immomio.data.landlord.entity.selfdisclosure.SelfDisclosureSubQuestionType;
import de.immomio.data.shared.bean.common.S3File;
import de.immomio.data.shared.entity.selfdisclosure.json.SelfDisclosureAnswers;
import de.immomio.data.shared.entity.selfdisclosure.json.SelfDisclosureQuestionAnswer;
import de.immomio.data.shared.entity.selfdisclosure.json.SelfDisclosureResponseData;
import de.immomio.data.shared.entity.selfdisclosure.json.SelfDisclosureSubQuestionAnswer;
import de.immomio.data.shared.entity.selfdisclosure.json.answertype.AddressAnswer;
import de.immomio.data.shared.entity.selfdisclosure.json.answertype.ChildrenAnswer;
import de.immomio.data.shared.entity.selfdisclosure.json.answertype.EmploymentAnswer;
import de.immomio.data.shared.entity.selfdisclosure.json.answertype.PersonAnswer;
import de.immomio.data.shared.entity.selfdisclosure.json.answertype.PersonsAnswer;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author Niklas Lindemann
 */
public class SelfDisclosureResponseJsonTest {

    @Test
    public void parseResponse() throws IOException {
        SdResponseApiBean sdResponseApiBean = new SdResponseApiBean();
        sdResponseApiBean.setId("123");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // StdDateFormat is ISO8601 since jackson 2.9
        objectMapper.setDateFormat(new StdDateFormat().withColonInTimeZone(true));
        PropertyData propertyData = objectMapper.readValue("{\"name\": \"Schöne 3-Zimmer-Wohnung mit Blick ins Grüne\", \"size\": 80.99, \"floor\": 0, \"rooms\": 3.0, \"garden\": false, \"ground\": null, \"heater\": \"GAS\", \"address\": {\"city\": \"Sylt\", \"region\": \"Schleswig-Holstein\", \"street\": \"Lüng Wai\", \"country\": \"DE\", \"zipCode\": \"25996\", \"additional\": \"\", \"coordinates\": {\"latitude\": 0.0, \"longitude\": 0.0}, \"houseNumber\": \"8\"}, \"balcony\": false, \"contact\": {\"name\": \"Putzler\", \"email\": \"hausbetreuung-putzler@t-online.de\", \"phone\": null, \"mobile\": \"0171-6253514\", \"address\": {\"city\": null, \"region\": null, \"street\": null, \"country\": null, \"zipCode\": null, \"additional\": null, \"coordinates\": null, \"houseNumber\": null}, \"firstName\": null}, \"bailment\": 1800.0, \"elevator\": false, \"flatType\": \"FLOOR\", \"basePrice\": 600.13, \"bathRooms\": 1, \"documents\": [], \"flatShare\": false, \"externalId\": null, \"attachments\": [{\"url\": \"https://s3.eu-central-1.amazonaws.com/immomio-dev-img-store/IMG-861a2c00a351495bac951908ac3582a9.jpg\", \"type\": \"IMG\", \"title\": \"Scannen0004.jpg\", \"encrypted\": false, \"extension\": \"jpeg\", \"identifier\": \"861a2c00a351495bac951908ac3582a9\"}], \"barrierFree\": false, \"heatingCost\": 180.0, \"kitchenette\": false, \"referenceId\": null, \"showAddress\": true, \"showContact\": true, \"basementSize\": null, \"parkingSpace\": false, \"availableFrom\": \"Thu Sep 01 10:16:57 CEST 2016\", \"guestToilette\": false, \"serviceCharge\": 110.0, \"constructionYear\": null, \"historicBuilding\": false, \"shortDescription\": \"Die hier zu vermietende drei Zimmer Wohnung liegt in einer großzügigen Anlage bestehend aus 6 Häusern welche 2001 im Friesenstil erbaut wurden. \\n\\nDie Häuser liegen eingebettet zwischen Friesenwällen und großzügigen Gartenanlagen. Pro Hauseingang wohnen 4 Familien.\", \"basementAvailable\": false, \"buildingCondition\": null, \"energyCertificate\": {\"creationDate\": \"APRIL_2014\", \"usageCertificate\": {\"energyConsumption\": null, \"energyEfficiencyClass\": null, \"includesHeatConsumption\": false, \"energyConsumptionParameter\": \"78\"}, \"demandCertificate\": null, \"yearOfConstruction\": 2001, \"energyCertificateType\": \"USAGE_IDENTIFICATION\", \"primaryEnergyProvider\": \"GAS\"}, \"lastRefurbishment\": null, \"objectDescription\": \"Die Wohnungen wurden 2001 aufwendig und hochwertig erbaut. Das Vollbad ist weiß gefliest. Die Küche ist mit einer Einbauküche ausgestattet. \\n\\nZu jeder Wohnung gehört ein Dachboden.\", \"objectLocationText\": \"Die Wohnanlage liegt an einer Privatstraße, zentral aber ruhig belegen. Der Golf Club Sylt grenzt direkt an den rückwärtigen Grundstücksteil. Der Ortskern von Wenningstedt ist ca. 1 km entfernt.\", \"heatingCostIncluded\": false, \"furnishingDescription\": null, \"objectMiscellaneousText\": \"Im Mietvertrag wird eine Staffelmiete vereinbart. Für die Anmietung ist ein Wohnberechtigungsschein notwendig.\"}", PropertyData.class);
        SelfDisclosureResponseData selfDisclosureResponseData = objectMapper.readValue("{\"answers\": {\"documents\": [\"Bonitätsauskunft\", \"Gehaltsnachweis\", \"Arbeitsvertrag\"], \"questions\": [{\"id\": 1242575, \"type\": \"PERSON\", \"title\": \"SELF_DISCLOSURE_PERSONAL_INFO_L\", \"answer\": {\"lastName\": \"Gersten\", \"birthDate\": \"1987-11-08\", \"birthName\": null, \"firstName\": \"Frank\", \"birthPlace\": \"München\"}, \"upload\": null, \"subQuestions\": [{\"id\": null, \"type\": \"ADDRESS\", \"title\": \"SELF_DISCLOSURE_CURRENT_ADDRESS_L\", \"answer\": {\"city\": \"München\", \"email\": \"frank.gersten@email.com\", \"phone\": \"01589224545\", \"street\": \"Rhinstrasse\", \"zipCode\": \"80902\", \"houseNumber\": \"43\"}, \"upload\": null, \"comment\": null, \"commentHint\": null, \"commentAllowed\": false, \"showSelfDisclosureQuestions\": true}, {\"id\": null, \"type\": \"EMPLOYMENT\", \"title\": \"SELF_DISCLOSURE_EMPLOYMENT_L\", \"answer\": {\"job\": \"Klempner\", \"employer\": \"Haustechnik GmbH\", \"netIncome\": 2200}, \"upload\": null, \"comment\": null, \"commentHint\": null, \"commentAllowed\": false, \"showSelfDisclosureQuestions\": true}, {\"id\": null, \"type\": \"BOOLEAN\", \"title\": \"SELF_DISCLOSURE_PAYMENT_OBLIGATIONS_L\", \"answer\": true, \"upload\": null, \"comment\": \"bis 20.01.2025\\n300 €/ Monat\", \"commentHint\": \"SELF_DISCLOSURE_PAYMENT_OBLIGATIONS_COMMENT_HINT_L\", \"commentAllowed\": true, \"showSelfDisclosureQuestions\": true}, {\"id\": null, \"type\": \"BOOLEAN\", \"title\": \"SELF_DISCLOSURE_LIST_OF_DEBTORS_L\", \"answer\": false, \"upload\": null, \"comment\": null, \"commentHint\": null, \"commentAllowed\": false, \"showSelfDisclosureQuestions\": true}, {\"id\": null, \"type\": \"BOOLEAN\", \"title\": \"SELF_DISCLOSURE_PAST_EVICTIONS_L\", \"answer\": false, \"upload\": null, \"comment\": null, \"commentHint\": null, \"commentAllowed\": false, \"showSelfDisclosureQuestions\": true}, {\"id\": null, \"type\": \"BOOLEAN\", \"title\": \"SELF_DISCLOSURE_PRIVATE_BANKRUPTCY_L\", \"answer\": false, \"upload\": null, \"comment\": null, \"commentHint\": null, \"commentAllowed\": false, \"showSelfDisclosureQuestions\": true}, {\"id\": null, \"type\": \"DOCUMENT\", \"title\": \"SELF_DISCLOSURE_DOCUMENT_INCOME\", \"answer\": null, \"upload\": {\"url\": \"https://s3.eu-central-1.amazonaws.com/immomio-stg-shared-doc-store/PDF-38ea90230074410092c7166b03665c7d.png\", \"name\": \"einkommensnachweis.png\", \"type\": \"SHARED_DOCUMENT\", \"index\": 0, \"title\": \"einkommensnachweis.png\", \"encrypted\": false, \"extension\": \"png\", \"identifier\": \"38ea90230074410092c7166b03665c7d\"}, \"comment\": null, \"commentHint\": null, \"commentAllowed\": null, \"showSelfDisclosureQuestions\": true}, {\"id\": null, \"type\": \"DOCUMENT\", \"title\": \"SELF_DISCLOSURE_DOCUMENT_CREDIT_RATING\", \"answer\": null, \"upload\": {\"url\": \"https://s3.eu-central-1.amazonaws.com/immomio-stg-shared-doc-store/PDF-e37e8582b37d4f3aa03e0547b6c006be.png\", \"name\": \"schufa.png\", \"type\": \"SHARED_DOCUMENT\", \"index\": 0, \"title\": \"schufa.png\", \"encrypted\": false, \"extension\": \"png\", \"identifier\": \"e37e8582b37d4f3aa03e0547b6c006be\"}, \"comment\": null, \"commentHint\": null, \"commentAllowed\": null, \"showSelfDisclosureQuestions\": true}], \"confirmations\": [{\"text\": \"Ich habe die Datenschutzhinweise zur Kenntnis genommen\", \"checked\": true}], \"uploadAllowed\": null, \"answerUnavailable\": null, \"showSelfDisclosureQuestions\": true}, {\"id\": 1242576, \"type\": \"PERSONS\", \"title\": \"SELF_DISCLOSURE_PERSONAL_INFO_TENANT_L\", \"answer\": [{\"id\": null, \"type\": null, \"title\": null, \"answer\": {\"lastName\": \"Koertig\", \"birthDate\": \"1977-09-09\", \"birthName\": null, \"firstName\": \"Johanna\", \"birthPlace\": \"Grünhain\"}, \"subQuestions\": [{\"id\": null, \"type\": \"ADDRESS\", \"title\": \"SELF_DISCLOSURE_CURRENT_ADDRESS_L\", \"answer\": {\"city\": \"Grünhain\", \"email\": \"johanna.koertig@mail.com\", \"phone\": \"01665875455\", \"street\": \"Schoenebergerstrasse\", \"zipCode\": \"08358\", \"houseNumber\": \"90\"}, \"comment\": null, \"commentHint\": null, \"commentAllowed\": false, \"showSelfDisclosureQuestions\": true}, {\"id\": null, \"type\": \"EMPLOYMENT\", \"title\": \"SELF_DISCLOSURE_EMPLOYMENT_L\", \"answer\": {\"job\": \"Erzieherin\", \"employer\": \"Kleine Kita e.V.\", \"netIncome\": 1400}, \"comment\": null, \"commentHint\": null, \"commentAllowed\": false, \"showSelfDisclosureQuestions\": true}, {\"id\": null, \"type\": \"BOOLEAN\", \"title\": \"SELF_DISCLOSURE_PAYMENT_OBLIGATIONS_L\", \"answer\": false, \"upload\": null, \"comment\": null, \"options\": null, \"uploadHint\": \"\", \"commentHint\": \"SELF_DISCLOSURE_PAYMENT_OBLIGATIONS_COMMENT_HINT_L\", \"uploadAllowed\": false, \"commentAllowed\": true, \"showSelfDisclosureQuestions\": true}, {\"id\": null, \"type\": \"BOOLEAN\", \"title\": \"SELF_DISCLOSURE_LIST_OF_DEBTORS_L\", \"answer\": false, \"upload\": null, \"comment\": null, \"options\": null, \"uploadHint\": \"\", \"commentHint\": null, \"uploadAllowed\": false, \"commentAllowed\": false, \"showSelfDisclosureQuestions\": true}, {\"id\": null, \"type\": \"BOOLEAN\", \"title\": \"SELF_DISCLOSURE_PAST_EVICTIONS_L\", \"answer\": true, \"upload\": null, \"comment\": null, \"options\": null, \"uploadHint\": \"\", \"commentHint\": null, \"uploadAllowed\": false, \"commentAllowed\": false, \"showSelfDisclosureQuestions\": true}, {\"id\": null, \"type\": \"BOOLEAN\", \"title\": \"SELF_DISCLOSURE_PRIVATE_BANKRUPTCY_L\", \"answer\": true, \"upload\": null, \"comment\": null, \"options\": null, \"uploadHint\": \"\", \"commentHint\": null, \"uploadAllowed\": false, \"commentAllowed\": false, \"showSelfDisclosureQuestions\": true}, {\"id\": null, \"type\": \"DOCUMENT\", \"title\": \"SELF_DISCLOSURE_DOCUMENT_INCOME\", \"answer\": null, \"upload\": {\"url\": \"https://s3.eu-central-1.amazonaws.com/immomio-stg-shared-doc-store/PDF-e91b9395b677436e93a7dbcc9472a098.png\", \"name\": \"einkommensnachweis.png\", \"type\": \"SHARED_DOCUMENT\", \"title\": \"einkommensnachweis.png\", \"encrypted\": false, \"extension\": \"png\", \"identifier\": \"e91b9395b677436e93a7dbcc9472a098\"}, \"comment\": null, \"commentHint\": null, \"commentAllowed\": null, \"showSelfDisclosureQuestions\": true}, {\"id\": null, \"type\": \"DOCUMENT\", \"title\": \"SELF_DISCLOSURE_DOCUMENT_CREDIT_RATING\", \"answer\": null, \"upload\": {\"url\": \"https://s3.eu-central-1.amazonaws.com/immomio-stg-shared-doc-store/PDF-19878f3d6b2c4ecda4901dafa421e00b.png\", \"name\": \"schufa.png\", \"type\": \"SHARED_DOCUMENT\", \"title\": \"schufa.png\", \"encrypted\": false, \"extension\": \"png\", \"identifier\": \"19878f3d6b2c4ecda4901dafa421e00b\"}, \"comment\": null, \"commentHint\": null, \"commentAllowed\": null, \"showSelfDisclosureQuestions\": true}], \"confirmations\": [{\"text\": \"Ich habe die Datenschutzhinweise zur Kenntnis genommen\", \"checked\": true}], \"showSelfDisclosureQuestions\": null}], \"upload\": null, \"subQuestions\": [], \"confirmations\": [], \"uploadAllowed\": null, \"answerUnavailable\": false, \"showSelfDisclosureQuestions\": true}, {\"id\": 1242577, \"type\": \"CHILDREN\", \"title\": \"SELF_DISCLOSURE_CHILDREN_ADULT_L\", \"answer\": [{\"id\": null, \"type\": null, \"title\": null, \"answer\": {\"lastName\": \"Schiffer\", \"birthDate\": \"1996-05-01\", \"firstName\": \"Andrea\"}, \"showSelfDisclosureQuestions\": null}], \"upload\": null, \"subQuestions\": [], \"confirmations\": [], \"uploadAllowed\": null, \"answerUnavailable\": false, \"showSelfDisclosureQuestions\": true}, {\"id\": 1242578, \"type\": \"CHILDREN\", \"title\": \"SELF_DISCLOSURE_CHILDREN_MINOR_L\", \"answer\": [{\"id\": null, \"type\": null, \"title\": null, \"answer\": {\"lastName\": \"Oster\", \"birthDate\": \"2010-10-05\", \"firstName\": \"Peter\"}, \"showSelfDisclosureQuestions\": null}], \"upload\": null, \"subQuestions\": [], \"confirmations\": [], \"uploadAllowed\": null, \"answerUnavailable\": false, \"showSelfDisclosureQuestions\": true}, {\"id\": 1242579, \"type\": \"BOOLEAN\", \"title\": \"SELF_DISCLOSURE_DOCUMENT_WBS_L\", \"answer\": false, \"upload\": null, \"subQuestions\": [], \"confirmations\": [], \"uploadAllowed\": true, \"answerUnavailable\": null, \"showSelfDisclosureQuestions\": true}], \"description\": \"Hier stehen Infos zum Datenschutz\", \"confirmations\": [\"Ich habe die Datenschutzhinweise zur Kenntnis genommen\"], \"feedbackEmail\": \"\", \"uploadedDocuments\": []}}", SelfDisclosureResponseData.class);
        List<SelfDisclosureQuestionAnswer> questions = selfDisclosureResponseData.getAnswers().getQuestions();
        sdResponseApiBean.setProperty(new SdResponseProperty(propertyData));
        appendDocuments(selfDisclosureResponseData, sdResponseApiBean);
        questions.forEach(questionAnswer -> {
            if (questionAnswer.getAnswer() == null) {
                return;
            }

            switch (questionAnswer.getType()) {
                case PERSON:
                    PersonAnswer personAnswer = parseAnswer(questionAnswer.getAnswer(), PersonAnswer.class);
                    SdResponseTenant tenant = getTenant(questionAnswer, personAnswer);
                    fillSubQuestions(tenant, questionAnswer.getSubQuestions());
                    sdResponseApiBean.setTenant(tenant);
                    break;
                case PERSONS:
                    PersonsAnswer[] personAnswers = parseAnswer(questionAnswer.getAnswer(), PersonsAnswer[].class);
                    Arrays.asList(personAnswers).forEach(onePersonAnswer -> {
                        SdResponseTenant createdTenant = getTenant(questionAnswer, onePersonAnswer.getAnswer());
                        fillSubQuestions(createdTenant, onePersonAnswer.getSubQuestions());
                        sdResponseApiBean.getFellowTenants().add(createdTenant);
                    });

                    break;

                case CHILDREN:
                    ChildrenAnswer[] childrenAnswers = parseAnswer(questionAnswer.getAnswer(), ChildrenAnswer[].class);
                    Arrays.stream(childrenAnswers)
                            .map(ChildrenAnswer::getAnswer)
                            .forEach(childAnswer -> sdResponseApiBean.getFurtherResidents().add(childAnswer));
                    break;

                case BOOLEAN:
                    Boolean answer = parseAnswer(questionAnswer.getAnswer(), Boolean.class);
                    SdResponseCheckedAnswer checkedAnswer = new SdResponseCheckedAnswer(questionAnswer.getTitle(), answer, null);
                    sdResponseApiBean.getCheckedAnswers().add(checkedAnswer);
                    break;
            }

        });
        selfDisclosureResponseData.getAnswers().getConfirmations().forEach(sdResponseApiBean.getConfirmations()::add);
        String string = objectMapper.writeValueAsString(sdResponseApiBean);
        System.out.println(string);
        sdResponseApiBean.getFellowTenants();
    }

    private void appendDocuments(SelfDisclosureResponseData response, SdResponseApiBean apiBean) {
        SelfDisclosureAnswers selfDisclosureAnswers = response.getAnswers();
        List<S3File> s3Files = selfDisclosureAnswers.getUploadedDocuments();

        List<SelfDisclosureQuestionAnswer> questions = selfDisclosureAnswers.getQuestions();
        List<SdResponseDocumentAnswer> documents = apiBean.getDocuments();
        if (questions != null) {
            questions.forEach(selfDisclosureQuestionAnswer -> {
                if (selfDisclosureQuestionAnswer.getUpload() != null) {
                    documents.add(new SdResponseDocumentAnswer(selfDisclosureQuestionAnswer.getTitle(), selfDisclosureQuestionAnswer.getUpload().getFilename()));
                }

                List<SelfDisclosureSubQuestionAnswer> subQuestions = selfDisclosureQuestionAnswer.getSubQuestions();
                if (subQuestions != null) {
                    subQuestions.stream()
                            .filter(item -> item.getUpload() != null)
                            .forEach(subQuestionAnswer -> documents.add(new SdResponseDocumentAnswer(subQuestionAnswer.getTitle(), subQuestionAnswer.getUpload().getFilename())));

                }
            });
        }

    }

    private SdResponseTenant getTenant(SelfDisclosureQuestionAnswer questionAnswer, PersonAnswer personAnswer) {
        SdResponseTenant tenant = new SdResponseTenant();

        tenant.setPersonDetails(personAnswer);


        return tenant;
    }

    private void fillSubQuestions(SdResponseTenant tenant, List<SelfDisclosureSubQuestionAnswer> subQuestions) {
        subQuestions.stream()
                .filter(subQuestion -> subQuestion.getType() == SelfDisclosureSubQuestionType.ADDRESS)
                .findFirst()
                .map(subQuestionAnswer -> parseAnswer(subQuestionAnswer.getAnswer(), AddressAnswer.class))
                .ifPresent(tenant::setAddressDetails);

        subQuestions.stream()
                .filter(subQuestion -> subQuestion.getType() == SelfDisclosureSubQuestionType.EMPLOYMENT)
                .findFirst()
                .map(subQuestionAnswer -> parseAnswer(subQuestionAnswer.getAnswer(), EmploymentAnswer.class))
                .ifPresent(tenant::setEmploymentDetails);

        subQuestions.stream()
                .filter(subQuestion -> subQuestion.getType() == SelfDisclosureSubQuestionType.BOOLEAN)
                .forEach(subQuestionAnswer -> appendCheckedAnswer(tenant, subQuestionAnswer));
    }

    private void appendCheckedAnswer(SdResponseTenant tenant, SelfDisclosureSubQuestionAnswer subQuestionAnswer) {
        if (subQuestionAnswer.getAnswer() == null) {
            return;
        }
        Boolean answer = (Boolean) subQuestionAnswer.getAnswer();
        tenant.getCheckedAnswers().add(new SdResponseCheckedAnswer(subQuestionAnswer.getTitle(), answer, subQuestionAnswer.getComment()));
    }


    private <T> T parseAnswer(Object answer, Class<T> typeOfAnswer) {
        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                false);
        return objectMapper.convertValue(answer, typeOfAnswer);
    }

}
