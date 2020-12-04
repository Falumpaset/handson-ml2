package de.immomio.utils.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

public class ResourcePageableUtils {

    public static final String DEFAULT_VALUE_PAGE = "0";
    public static final String DEFAULT_VALUE_SIZE = "25";
    static final String SORT_PARAM_SEPARATOR = ",";

    public static Pageable pageableOf(
            int page,
            int size,
            String sortStr
    ) {
        Sort sort = Sort.unsorted();
        if (!StringUtils.isEmpty(sortStr)) {
            String sortBy = sortStr;
            Sort.Direction sortDir = Sort.DEFAULT_DIRECTION;
            if (sortStr.contains(SORT_PARAM_SEPARATOR)) {
                String[] sortParts = sortStr.split(SORT_PARAM_SEPARATOR);
                sortBy = sortParts[0];
                sortDir = Sort.Direction.fromString(sortParts[1]);
            }
            sort = Sort.by(sortDir, sortBy);
        }
        return PageRequest.of(page, size, sort);
    }

}
