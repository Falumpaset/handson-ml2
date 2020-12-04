package de.immomio.model.selfdisclosure;

/**
 * @author Niklas Lindemann
 */
public enum SelfDisclosureSubQuestionType {
    BOOLEAN(5),
    SELECT(3),
    CHILD(3),
    ADDRESS(1),
    EMPLOYMENT(2),
    FLAT(6),
    DOCUMENT(7);

    private Integer sort;

    SelfDisclosureSubQuestionType(Integer sort) {
        this.sort = sort;
    }

    public Integer getSort() {
        return sort;
    }
}
