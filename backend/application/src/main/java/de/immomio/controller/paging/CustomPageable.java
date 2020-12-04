package de.immomio.controller.paging;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.AbstractPageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.data.util.Pair;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class CustomPageable extends AbstractPageRequest {

    public static final String SORT_PARAM = "sort";

    private static final long serialVersionUID = -3428460521552799793L;

    private static final int DEFAULT_PAGE = 0;

    private static final int DEFAULT_SIZE = 25;

    private int page = DEFAULT_PAGE;

    private int size = DEFAULT_SIZE;

    private String[] sort;

    public CustomPageable() {
        super(DEFAULT_PAGE, DEFAULT_SIZE);
    }

    public CustomPageable(int page, int size) {
        super(page, size);
    }

    public CustomPageable(int page, int size, String[] sort) {
        super(page, size);
        this.page = page;
        this.size = size;
        this.sort = sort;
    }

    public Pageable next() {
        return new CustomPageable(this.getPageNumber() + 1, this.getPageSize(), this.sort);
    }

    public CustomPageable previous() {
        return this.getPageNumber() == 0 ? this : new CustomPageable(this.getPageNumber() - 1, this.getPageSize(),
                this.sort);
    }

    public Pageable first() {
        return new CustomPageable(0, this.getPageSize(), this.sort);
    }

    public Sort getSort() {
        JpaSort jpaSort = null;

        if (this.sort != null) {
            List<String> sortValues = Arrays.asList(this.sort);
            if (sortValues.size() > 0) {
                Pair<Sort.Direction, String> orderFields = parseOrderFields(sortValues.get(0));
                jpaSort = JpaSort.unsafe(orderFields.getFirst(), orderFields.getSecond());

                if (sortValues.size() > 1) {
                    for (String sortElem : sortValues.subList(1, sortValues.size())) {
                        orderFields = parseOrderFields(sortElem);
                        jpaSort = jpaSort.andUnsafe(orderFields.getFirst(), orderFields.getSecond());
                    }
                }
            }
        }

        return jpaSort;
    }

    @Override
    public String toString() {
        return String.format("Page request [number: %d, size %d, sort: %s]", this.getPageNumber(), this.getPageSize(),
                this.sort == null ? null : this.getSort().toString());
    }

    private Pair<Sort.Direction, String> parseOrderFields(String sort) {
        String[] attributes = sort.split(",");
        Sort.Direction direction = attributes.length > 1 ? Sort.Direction.valueOf(attributes[1].toUpperCase())
                : Sort.Direction.ASC;

        return Pair.of(direction, attributes[0]);
    }

}
