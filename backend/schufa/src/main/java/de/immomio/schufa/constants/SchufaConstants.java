package de.immomio.schufa.constants;

/**
 * @author Niklas Lindemann
 */
public class SchufaConstants {

    private SchufaConstants()
    {

    }

    public static final String DATA_KNOWN = "p01";
    public static final String SYNTAX_CORRECT = "s01";
    public static final String SYNTAX_NOT_CORRECT_INSTITUTE_KNOWN = "s02";
    public static final String SYNTAX_NOT_CORRECT = "s03";
    public static final String CHECK_DIGIT_PROCESS_NOT_AVAILABLE = "s04";
    public static final String CHECK_DIGIT_PROCESS_NOT_AVAILABLE_FOR_INSTITUTE = "s05";
    public static final String SYNTAX_CHECK_NEGATIVE = "p06";
    public static final String PAYMENT_ACCOUNT_KNOWN = "p02";
    public static final String BUSINESS_RELATIONSHIP_KNOWN = "p03";
    public static final String PERSON_IS_DEAD = "p04";
    public static final String SCHUFA_DATA_NOT_KNOWN = "p05";
    public static final String ERROR_IBAN_SYNTAX_FAILED = "VAL0078";
}
