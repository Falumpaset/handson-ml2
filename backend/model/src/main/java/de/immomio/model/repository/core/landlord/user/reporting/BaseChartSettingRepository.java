package de.immomio.model.repository.core.landlord.user.reporting;

import de.immomio.data.landlord.entity.user.reporting.ChartSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Niklas Lindemann
 */
@RepositoryRestResource(path = "chartSettings")
public interface BaseChartSettingRepository extends JpaRepository<ChartSetting, Long> {

}
