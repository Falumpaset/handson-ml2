package de.immomio.service.shared.contract;

import de.immomio.data.shared.bean.common.Address;
import de.immomio.data.shared.bean.contract.DigitalContractPropertyData;
import de.immomio.data.shared.entity.contract.DigitalContract;
import org.apache.commons.lang3.StringUtils;

public class DigitalContractFilenameHelper {

    public static final String UNDERSCORE = "_";
    public static final String SPACE = " ";
    public static final String DOT = ".";
    public static final String EMPTY = "";
    public static final String CONTRACT = "Vertrag";
    public static final String PDF = ".pdf";

    public static String getContractFilenameWithoutEnding(DigitalContract contract) {
        StringBuilder stringBuilder = new StringBuilder();
        DigitalContractPropertyData propertyData = contract.getPropertyData();
        Address address = propertyData.getAddress();

        append(stringBuilder, CONTRACT);
        append(stringBuilder, propertyData.getExternalId());
        append(stringBuilder, address.getCity());
        append(stringBuilder, address.getZipCode());
        append(stringBuilder, address.getStreet());
        append(stringBuilder, address.getHouseNumber());
        String fileName = stringBuilder.toString();

        return finalizeName(fileName);
    }

    public static void append(StringBuilder builder, String value) {
        if (StringUtils.isNotBlank(value)) {
            builder.append(value).append(UNDERSCORE);
        }
    }

    public static String finalizeName(String fileName) {
        return fileName
                .substring(0, fileName.length() - 1)
                .replace(SPACE, UNDERSCORE)
                .replace(DOT, EMPTY);
    }

}
