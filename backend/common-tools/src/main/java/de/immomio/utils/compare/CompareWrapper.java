package de.immomio.utils.compare;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author Niklas Lindemann
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompareWrapper {
    private Date date;
    private List<CompareBean> compareBeans;
}
