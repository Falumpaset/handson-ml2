package de.immomio.model.entity.admin.report;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.immomio.data.base.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author Niklas Lindemann
 */

@Entity
@Setter
@Getter
@EntityListeners(AuditingEntityListener.class)
@Table(name = "report", schema = "administration")
public class Report extends AbstractEntity {
    private static final long serialVersionUID = 7613829789102142059L;

    @CreatedDate
    private Date created;

    @LastModifiedDate
    private Date updated;

    private Date startdate;

    private Date enddate;

    private ReportType type;

    private TimespanType timespan;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String report;
}
