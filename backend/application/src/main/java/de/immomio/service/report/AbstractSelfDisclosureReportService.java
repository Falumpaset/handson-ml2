package de.immomio.service.report;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.immomio.common.amazon.s3.AbstractS3FileManager;
import de.immomio.common.file.FileUtilities;
import de.immomio.data.landlord.bean.property.data.PropertyData;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.selfdisclosure.SelfDisclosureQuestionType;
import de.immomio.data.shared.bean.common.Address;
import de.immomio.data.shared.bean.common.S3File;
import de.immomio.data.shared.entity.selfdisclosure.SelfDisclosureResponse;
import de.immomio.data.shared.entity.selfdisclosure.json.SelfDisclosureAnswers;
import de.immomio.data.shared.entity.selfdisclosure.json.SelfDisclosureConfirmation;
import de.immomio.data.shared.entity.selfdisclosure.json.SelfDisclosureQuestionAnswer;
import de.immomio.data.shared.entity.selfdisclosure.json.SelfDisclosureSubQuestionAnswer;
import de.immomio.data.shared.entity.selfdisclosure.json.answertype.AddressAnswer;
import de.immomio.data.shared.entity.selfdisclosure.json.answertype.ChildAnswer;
import de.immomio.data.shared.entity.selfdisclosure.json.answertype.ChildrenAnswer;
import de.immomio.data.shared.entity.selfdisclosure.json.answertype.EmploymentAnswer;
import de.immomio.data.shared.entity.selfdisclosure.json.answertype.PersonAnswer;
import de.immomio.data.shared.entity.selfdisclosure.json.answertype.PersonsAnswer;
import de.immomio.service.landlord.selfdisclosure.PdfMergerService;
import de.immomio.utils.FileStorageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractMessageSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import static de.immomio.service.report.SelfDisclosureReportParams.ANSWERS;
import static de.immomio.service.report.SelfDisclosureReportParams.PROPERTY_ADDRESS;
import static de.immomio.service.report.SelfDisclosureReportParams.PROPERTY_ADDRESS_TITLE;
import static de.immomio.service.report.SelfDisclosureReportParams.PROPERTY_DATA_TITLE;
import static de.immomio.service.report.SelfDisclosureReportParams.PROPERTY_NAME;
import static de.immomio.service.report.SelfDisclosureReportParams.PROPERTY_NAME_TITLE;
import static de.immomio.service.report.SelfDisclosureReportParams.REPORT_TITLE;
import static de.immomio.service.report.SelfDisclosureReportParams.UPLOADED_IMAGES;

/**
 * @author Niklas Lindemann
 */

@Slf4j
public abstract class AbstractSelfDisclosureReportService<ASM extends AbstractS3FileManager, ASD
        extends AbstractMessageSource> {

    private static final String TEMPLATE_NAME = "report/self-disclosure.jrxml";

    private static final String ADDRESS_FORMAT = "%s %s, %s %s";

    private static final List<String> SUPPORTED_IMAGE_TYPES = List.of("jpg", "jpeg", "png");

    private static final List<String> SUPPORTED_PDF_TYPES = List.of("pdf");

    private static final String RESPONSE_REPORT_NAME = "self-disclosure-response";

    private static final String SPACE = " ";
    @Autowired
    private PdfMergerService pdfMergerService;

    public abstract ASM getFileManager();

    public abstract ASD getMessageSource();

    public abstract JasperReportService getJasperReportService();

    public OutputStream generateReport(SelfDisclosureResponse response) throws FileNotFoundException {
        String reportTemplate = getReportTemplateFileName();
        Map<String, Object> params = generateModel(response);

        List<File> uploadedImages = getUploadedImages(response.getData().getAnswers());
        params.put(UPLOADED_IMAGES, uploadedImages);

        OutputStream outputStream = getJasperReportService().generatePdfReport(reportTemplate, params);
        if (outputStream != null) {
            pdfMergerService.appendDocuments(
                    extractDocumentsForType(response.getData().getAnswers(), SUPPORTED_PDF_TYPES),
                    outputStream, getFileManager());
        }
        uploadedImages.forEach(FileUtilities::forceDelete);

        return outputStream;
    }

    public String getResponseReportName(SelfDisclosureResponse selfDisclosureResponse) {
        return RESPONSE_REPORT_NAME + "-" + selfDisclosureResponse.getId();
    }

    private Map<String, Object> generateModel(SelfDisclosureResponse response) {
        Map<String, Object> model = new HashMap<>();

        populateBaseModel(model, response.getProperty());

        List<Map<String, Map<String, String>>> answers = new ArrayList<>();
        model.put(ANSWERS, answers);

        response.getData()
                .getAnswers()
                .getQuestions()
                .stream()
                .sorted(Comparator.comparing(o -> o.getType().getSort()))
                .forEach(questionAnswer -> {
                    SelfDisclosureQuestionType questionType = questionAnswer.getType();

                    writeAnswer(questionAnswer, questionType, answers);
                    writeSubAnswers(questionAnswer, answers);
                    writeConfirmations(questionAnswer, answers);
                });

        return model;
    }

    private void writeConfirmations(SelfDisclosureQuestionAnswer questionAnswer,
            List<Map<String, Map<String, String>>> answers) {
        if (!questionAnswer.getConfirmations().isEmpty()) {
            Map<String, Map<String, String>> answer = createAndAdd(answers);

            Map<String, String> data = new LinkedHashMap<>();
            answer.put(translate("selfdisclosure.report.confirmations"), data);

            questionAnswer.getConfirmations().forEach(confirmation -> writeConfirmation(confirmation, data));
        }
    }

    private void writeSubAnswers(SelfDisclosureQuestionAnswer questionAnswer,
            List<Map<String, Map<String, String>>> answers) {
        if (questionAnswer.getSubQuestions() != null) {
            questionAnswer.getSubQuestions()
                    .stream()
                    .sorted(Comparator.comparing(subQuestionAnswer -> subQuestionAnswer.getType().getSort()))
                    .forEach(selfDisclosureSubQuestionAnswer -> {
                        Map<String, Map<String, String>> answer = createAndAdd(answers);

                        Map<String, String> data = new LinkedHashMap<>();
                        answer.put(translate(selfDisclosureSubQuestionAnswer.getTitle()), data);

                        writeSubAnswer(selfDisclosureSubQuestionAnswer, data);
                    });
        }
    }

    private void writeAnswer(SelfDisclosureQuestionAnswer questionAnswer,
            SelfDisclosureQuestionType type,
            List<Map<String, Map<String, String>>> answers) {
        if (questionAnswer.getAnswer() != null) {
            switch (type) {
                case PERSON:
                    Map<String, Map<String, String>> answer = createAndAdd(answers);

                    Map<String, String> data = new LinkedHashMap<>();
                    answer.put(translate(questionAnswer.getTitle()), data);

                    writePersonData(parseAnswer(questionAnswer.getAnswer(), PersonAnswer.class), data);
                    break;
                case PERSONS:
                    writePersonsData(parseAnswer(questionAnswer.getAnswer(), PersonsAnswer[].class), answers);
                    break;
                case CHILDREN:
                    writeChildrenData(parseAnswer(questionAnswer.getAnswer(), ChildrenAnswer[].class), questionAnswer.getTitle(), answers);
                    break;
                default: // ignore
            }
        }
    }

    private void writeSubAnswer(SelfDisclosureSubQuestionAnswer answer, Map<String, String> data) {
        if (answer.getAnswer() != null) {
            try {
                switch (answer.getType()) {
                    case ADDRESS:
                        writeAddressData(parseAnswer(answer.getAnswer(), AddressAnswer.class), data);
                        break;
                    case CHILD:
                        writeChildData(parseAnswer(answer.getAnswer(), ChildAnswer.class), data);
                        break;
                    case EMPLOYMENT:
                        writeEmploymentData(parseAnswer(answer.getAnswer(), EmploymentAnswer.class), data);
                        break;
                    case BOOLEAN:
                        writeBooleanData(answer, data);
                        break;
                    case SELECT:
                        writeSelectData(answer, data);
                        break;
                    default: // ignore
                }
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }
        }
    }

    private void writeConfirmation(SelfDisclosureConfirmation confirmation, Map<String, String> data) {
        data.put(confirmation.getText(), translateBoolean(confirmation.getChecked()));
    }

    private void populateBaseModel(Map<String, Object> model, Property property) {
        model.put(REPORT_TITLE, translate("SELF_DISCLOSURE_REPORT_TITLE_L"));
        model.put(PROPERTY_DATA_TITLE, translate("SELF_DISCLOSURE_PROPERTY_INFO_L"));
        model.put(PROPERTY_NAME_TITLE, translate("selfdisclosure.report.flat.name"));
        model.put(PROPERTY_NAME, parsePropertyName(property));
        model.put(PROPERTY_ADDRESS_TITLE, translate("selfdisclosure.report.address"));
        model.put(PROPERTY_ADDRESS, formattedAddress(property.getData().getAddress()));
    }

    private String formattedAddress(Address address) {
        return String.format(ADDRESS_FORMAT,
                address.getStreet(),
                address.getHouseNumber(),
                address.getZipCode(),
                address.getCity());
    }

    private String formattedAddress(AddressAnswer address) {
        return String.format(ADDRESS_FORMAT,
                address.getStreet(),
                address.getHouseNumber(),
                address.getZipCode(),
                address.getCity());
    }

    private void writeChildrenData(ChildrenAnswer[] childrenAnswers, String title, List<Map<String, Map<String, String>>> answers) {
        Arrays.asList(childrenAnswers).forEach(childAnswer -> {
            Map<String, Map<String, String>> answer = createAndAdd(answers);

            Map<String, String> data = new LinkedHashMap<>();
            answer.put(translate(title), data);

            writeChildData(childAnswer.getAnswer(), data);
        });
    }

    private void writeChildData(ChildAnswer childAnswer, Map<String, String> data) {
        String fullName = childAnswer.getFirstName() + " " + childAnswer.getLastName();
        data.put(translate("selfdisclosure.report.tenant.name"), fullName);
        data.put(translate("selfdisclosure.report.tenant.birthday"), formatDate(childAnswer.getBirthDate()));
    }

    private void writePersonData(PersonAnswer answer, Map<String, String> data) {
        data.put(translate("selfdisclosure.report.tenant.name"), formatName(answer));
        data.put(translate("selfdisclosure.report.tenant.birthday"), formatDate(answer.getBirthDate()));
        data.put(translate("selfdisclosure.report.tenant.birthplace"), answer.getBirthPlace());
    }

    private String formatDate(Date date) {
        if (date != null) {
            return DateFormat.getDateInstance(0, Locale.GERMAN).format(date);
        }

        return null;
    }

    private String formatAmount(Double amount) {
        if (amount != null) {
            return NumberFormat.getCurrencyInstance(Locale.GERMANY).format(amount);
        }

        return null;
    }

    private void writePersonsData(PersonsAnswer[] personsAnswers, List<Map<String, Map<String, String>>> answers) {
        Arrays.asList(personsAnswers).forEach(personsAnswer -> {
            Map<String, Map<String, String>> answer = createAndAdd(answers);

            Map<String, String> data = new LinkedHashMap<>();
            answer.put(translate("SELF_DISCLOSURE_PERSONAL_INFO_TENANT_L"), data);

            writePersonData(personsAnswer.getAnswer(), data);
            personsAnswer.getSubQuestions()
                    .stream()
                    .sorted(Comparator.comparing(item -> item.getType().getSort()))
                    .forEach(subQuestionAnswer -> writeSubAnswer(subQuestionAnswer, data));
        });
    }

    private void writeAddressData(AddressAnswer addressAnswer, Map<String, String> data) {
        data.put(translate("selfdisclosure.report.tenant.address"), formattedAddress(addressAnswer));
        data.put(translate("selfdisclosure.report.tenant.telephone"), addressAnswer.getPhone());
        data.put(translate("selfdisclosure.report.tenant.email"), addressAnswer.getEmail());
    }

    private void writeEmploymentData(EmploymentAnswer employmentAnswer, Map<String, String> data) {
        data.put(translate("selfdisclosure.report.tenant.employer"), employmentAnswer.getEmployer());
        data.put(translate("selfdisclosure.report.tenant.job"), employmentAnswer.getJob());
        data.put(translate("selfdisclosure.report.tenant.income"), formatAmount(employmentAnswer.getNetIncome()));
    }

    private void writeBooleanData(SelfDisclosureSubQuestionAnswer answer, Map<String, String> data) {
        String label = translate(answer.getTitle());
        String value = translateBoolean(parseAnswer(answer.getAnswer(), Boolean.class));
        data.put(label, value);
        if (StringUtils.isNotBlank(answer.getCommentHint()) && StringUtils.isNotBlank(answer.getComment())) {
            data.put(translate(answer.getCommentHint()), answer.getComment());
        }
    }

    private void writeSelectData(SelfDisclosureSubQuestionAnswer answer, Map<String, String> data) {
        String label = translate(answer.getTitle());
        String value = translate(answer.getAnswer().toString());
        data.put(label, value);
        if (StringUtils.isNotBlank(answer.getCommentHint()) && StringUtils.isNotBlank(answer.getComment())) {
            data.put(translate(answer.getCommentHint()), answer.getComment());
        }
    }

    private <T> T parseAnswer(Object answer, Class<T> typeOfAnswer) {
        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                false);
        return objectMapper.convertValue(answer, typeOfAnswer);
    }

    private String formatName(PersonAnswer answer) {
        String firstName = getTextOrEmpty(answer.getFirstName());
        String lastName = getTextOrEmpty(answer.getLastName());
        String result = firstName + SPACE + lastName;

        if (StringUtils.isBlank(answer.getBirthName())) {
            return result;
        } else {
            return result + SPACE + translate("selfdisclosure.report.tenant.birthname", answer.getBirthName());
        }
    }

    private String getTextOrEmpty(String text) {
        return text != null ? text : "";
    }

    private String translateBoolean(Boolean bool) {
        if (BooleanUtils.isTrue(bool)) {
            return translate("selfdisclosure.report.yes");
        } else {
            return translate("selfdisclosure.report.no");
        }
    }

    private String parsePropertyName(Property property) {
        PropertyData data = property.getData();
        String result = data.getName();

        if (StringUtils.isNotBlank(property.getExternalId())) {
            result = result + String.format("(%s)", property.getExternalId());
        }

        return result;
    }

    private List<S3File> extractDocumentsForType(SelfDisclosureAnswers answers, List<String> supportedTypes) {
        List<S3File> documents = new ArrayList<>();
        List<SelfDisclosureQuestionAnswer> questions = answers.getQuestions();
        if (questions != null) {
            questions.forEach(questionAnswer -> {
                if (questionAnswer.getUpload() != null) {
                    documents.add(questionAnswer.getUpload());
                }

                questionAnswer.getSubQuestions()
                        .stream()
                        .filter(subQuestionAnswer -> subQuestionAnswer.getUpload() != null)
                        .forEach(subQuestionAnswer -> {
                            documents.add(subQuestionAnswer.getUpload());
                        });
                if (questionAnswer.getType() == SelfDisclosureQuestionType.PERSONS) {
                    PersonsAnswer[] personsAnswers = parseAnswer(questionAnswer.getAnswer(), PersonsAnswer[].class);
                    Arrays.stream(personsAnswers).forEach(personsAnswer -> {
                        personsAnswer.getSubQuestions().stream()
                                .filter(subQuestionAnswer -> subQuestionAnswer.getUpload() != null)
                                .forEach(subQuestionAnswer -> {
                                    documents.add(subQuestionAnswer.getUpload());
                                });
                    });
                }
            });
        }
        documents.addAll(answers.getUploadedDocuments());
        return documents
                .stream()
                .filter(s3File -> supportedTypes.contains(s3File.getExtension()))
                .collect(Collectors.toList());
    }

    private List<File> getUploadedImages(SelfDisclosureAnswers answers) {
        List<S3File> s3Files = extractDocumentsForType(answers, SUPPORTED_IMAGE_TYPES);
        List<File> images = new ArrayList<>();
        s3Files.stream().filter(s3File -> SUPPORTED_IMAGE_TYPES.contains(s3File.getExtension())).forEach(s3File -> {
            try {
                File file = FileStorageUtils.downloadFile(s3File.getUrl(), getFileManager(), false, null);
                images.add(file);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });

        return images;
    }

    private String translate(String key, String... substitutions) {
        return getMessageSource().getMessage(key, substitutions, Locale.GERMAN);
    }

    private String getReportTemplateFileName() {
        URL reportTemplateResource = this.getClass().getClassLoader().getResource(TEMPLATE_NAME);
        return reportTemplateResource.getFile();
    }

    private Map<String, Map<String, String>> createAndAdd(List<Map<String, Map<String, String>>> answers) {
        Map<String, Map<String, String>> answer = new LinkedHashMap<>();
        answers.add(answer);

        return answer;
    }
}
