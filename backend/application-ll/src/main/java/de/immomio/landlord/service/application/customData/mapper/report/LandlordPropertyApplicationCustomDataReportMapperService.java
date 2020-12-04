package de.immomio.landlord.service.application.customData.mapper.report;

import de.immomio.config.ApplicationMessageSource;
import de.immomio.constants.exceptions.ImmomioRuntimeException;
import de.immomio.data.base.type.application.ApplicationCustomDataAnswerType;
import de.immomio.data.base.type.application.ApplicationCustomDataFieldType;
import de.immomio.data.landlord.bean.application.customData.ApplicationCustomDataBean;
import de.immomio.data.landlord.bean.application.customData.ApplicationCustomDataBundle;
import de.immomio.data.landlord.bean.application.customData.ApplicationCustomDataFieldAnswerBean;
import de.immomio.data.landlord.bean.application.customData.ApplicationCustomDataFieldBean;
import de.immomio.data.landlord.entity.property.Property;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Service
public class LandlordPropertyApplicationCustomDataReportMapperService {

    private static final String CUSTOM_DATA_NO_DATA = "CustomData.no_data";
    private static final String CUSTOM_DATA_DK_HIDDEN = "CustomData.dk_hidden";
    private static final String BOOLEAN_TRUE = "boolean.true";
    private static final String BOOLEAN_FALSE = "boolean.false";
    private static final String DD_MM_YYYY = "dd.mm.yyyy";
    private static final String CUSTOM_DATA_CUSTOM_QUESTION_DATE = "CustomData.custom_question.date";
    private static final String CUSTOM_DATA_CUSTOM_QUESTION_NUMBER = "CustomData.custom_question.number";

    private final ApplicationMessageSource messageSource;

    @Autowired
    public LandlordPropertyApplicationCustomDataReportMapperService(ApplicationMessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public ByteArrayOutputStream writeDataToStream(ApplicationCustomDataBundle answerBundle, Property property) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();

        fillSheet(sheet, answerBundle, property);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException e) {
            throw new ImmomioRuntimeException();
        }

        return outputStream;
    }

    private void fillSheet(XSSFSheet sheet, ApplicationCustomDataBundle answerBundle, Property property) {
        AtomicInteger rowNumber = new AtomicInteger(0);

        createPropertyHeader(rowNumber, sheet, property);
        createHeader(rowNumber, sheet, answerBundle.getFields());
        answerBundle.getData().forEach(data -> addData(rowNumber, sheet, answerBundle.getFields(), data));
    }

    private void createPropertyHeader(AtomicInteger rowNumber, XSSFSheet sheet, Property property) {
        XSSFRow row = sheet.createRow(rowNumber.getAndAdd(1));

        int cellNumber = 0;
        String externalId = property.getExternalId();
        if (externalId != null) {
            XSSFCell propertyExternalIdCell = row.createCell(cellNumber++);
            propertyExternalIdCell.setCellValue(externalId);
        }
        XSSFCell propertyAddressCell = row.createCell(cellNumber++);
        propertyAddressCell.setCellValue(property.getData().getAddress().toString());
    }

    private void createHeader(AtomicInteger rowNumber, XSSFSheet sheet, List<ApplicationCustomDataFieldBean> fields) {
        XSSFRow row = sheet.createRow(rowNumber.getAndAdd(1));

        AtomicInteger cellNumber = new AtomicInteger(0);
        fields.stream().filter(field -> shouldAnswerTypeBeAdded(field.getAnswerType())).forEach(field -> addHeaderCells(cellNumber, row, field));
    }

    private void addHeaderCells(AtomicInteger position, Row row, ApplicationCustomDataFieldBean field) {
        String cellName = field.getFieldName();
        if (field.getFieldType() == ApplicationCustomDataFieldType.DATA) {
            cellName = createTranslatedAnswer(cellName);
        }

        addHeaderCell(position, row, cellName);
        switch (field.getAnswerType()) {
            case STRING_DATE -> addHeaderCell(position, row, cellName + " " + createTranslatedAnswer(CUSTOM_DATA_CUSTOM_QUESTION_DATE));
            case STRING_NUMBER -> addHeaderCell(position, row, cellName + " " + createTranslatedAnswer(CUSTOM_DATA_CUSTOM_QUESTION_NUMBER));
        }
    }

    private void addHeaderCell(AtomicInteger position, Row row, String cellName) {
        Cell cell = row.createCell(position.getAndAdd(1));
        cell.setCellValue(cellName);
    }

    private void addData(AtomicInteger rowNumber, XSSFSheet sheet, List<ApplicationCustomDataFieldBean> fields, ApplicationCustomDataBean data) {
        XSSFRow row = sheet.createRow(rowNumber.getAndAdd(1));

        AtomicInteger cellNumber = new AtomicInteger(0);
        IntStream.range(0, fields.size()).forEach(i -> {
            ApplicationCustomDataFieldBean field = fields.get(i);
            addDataCells(cellNumber, row, field.getAnswerType(), data.getFieldData().get(i));
        });
    }

    private void addDataCells(AtomicInteger position, Row row, ApplicationCustomDataAnswerType answerType, ApplicationCustomDataFieldAnswerBean answer) {
        if (!shouldAnswerTypeBeAdded(answerType)) {
            return;
        }

        switch (answerType) {
            case STRING_DATE -> {
                addSingleDataCells(position, row, ApplicationCustomDataAnswerType.STRING, answer);
                addSingleDataCells(position, row, ApplicationCustomDataAnswerType.DATE, answer);
            }
            case STRING_NUMBER -> {
                addSingleDataCells(position, row, ApplicationCustomDataAnswerType.STRING, answer);
                addSingleDataCells(position, row, ApplicationCustomDataAnswerType.NUMBER, answer);
            }
            default -> addSingleDataCells(position, row, answerType, answer);
        }
    }

    private boolean shouldAnswerTypeBeAdded(ApplicationCustomDataAnswerType answerType) {
        return answerType != ApplicationCustomDataAnswerType.FILE;
    }

    private void addSingleDataCells(AtomicInteger position, Row row, ApplicationCustomDataAnswerType answerType, ApplicationCustomDataFieldAnswerBean answer) {
        Cell cell = row.createCell(position.getAndAdd(1));
        boolean change = false;

        if (BooleanUtils.isTrue(answer.getHidden())) {
            change = true;
            cell.setCellValue(createTranslatedAnswer(CUSTOM_DATA_DK_HIDDEN));
        }

        switch (answerType) {
            case DATE -> {
                if (answer.getDateAnswer() != null) {
                    change = true;
                    cell.setCellValue(answer.getDateAnswer());
                    CellStyle cellStyle = cell.getSheet().getWorkbook().createCellStyle();
                    cellStyle.setDataFormat(cell.getSheet().getWorkbook().getCreationHelper().createDataFormat().getFormat(DD_MM_YYYY));
                    cell.setCellStyle(cellStyle);
                }
            }
            case BOOLEAN -> {
                if (answer.getBooleanAnswer() != null) {
                    change = true;
                    cell.setCellValue(createTranslatedAnswer(BooleanUtils.isTrue(answer.getBooleanAnswer()) ? BOOLEAN_TRUE : BOOLEAN_FALSE));
                }
            }
            case STRING -> {
                if (answer.getStringAnswer() != null && StringUtils.isNotEmpty(answer.getStringAnswer())) {
                    change = true;
                    cell.setCellValue(answer.getStringAnswer());
                }
            }
            case ENUM -> {
                if (answer.getStringAnswer() != null && StringUtils.isNotEmpty(answer.getStringAnswer())) {
                    change = true;
                    cell.setCellValue(createTranslatedAnswer(answer.getStringAnswer()));
                }
            }
            case NUMBER -> {
                if (answer.getNumberAnswer() != null) {
                    change = true;
                    cell.setCellValue(answer.getNumberAnswer().doubleValue());
                }
            }
            default -> throw new ImmomioRuntimeException();
        }

        if (!change) {
            cell.setCellValue(createTranslatedAnswer(CUSTOM_DATA_NO_DATA));
        }
    }

    private String createTranslatedAnswer(String key) {
        return messageSource.resolveCodeString(key, Locale.GERMAN);
    }
}
