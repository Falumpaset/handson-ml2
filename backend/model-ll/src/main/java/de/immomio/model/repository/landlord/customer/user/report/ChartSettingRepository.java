package de.immomio.model.repository.landlord.customer.user.report;

import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.landlord.entity.user.reporting.ChartSetting;
import de.immomio.data.landlord.entity.user.reporting.enums.ReportChart;
import de.immomio.model.repository.core.landlord.user.reporting.BaseChartSettingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
import java.util.Optional;

/**
 * @author Niklas Lindemann
 */
public interface ChartSettingRepository extends BaseChartSettingRepository {

    @Override
    @RestResource(exported = false)
    Page<ChartSetting> findAll(Pageable pageable);

    @Override
    @RestResource(exported = false)
    <S extends ChartSetting> S save(S entity);

    @Override
    @RestResource(exported = false)
    Optional<ChartSetting> findById(Long id);

    @Override
    @RestResource(exported = false)
    void delete(ChartSetting entity);

    @RestResource(exported = false)
    Optional<ChartSetting> findByUserAndChart(LandlordUser user, ReportChart reportChart);

    @Query("SELECT o from ChartSetting o where o.user.id = ?#{principal.id}")
    List<ChartSetting> findByUser();
}
