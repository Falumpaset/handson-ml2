package de.immomio.beans.landlord.product.quota;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Niklas Lindemann
 */

@Data
@Builder
public class QuotaProductBean implements Serializable {
    private static final long serialVersionUID = 2860649751320590559L;
    private Long usedQuota;

    private Long totalQuota;

    @JsonProperty
    public Long getAvailableQuota() {
        return totalQuota - usedQuota;
    }
}
