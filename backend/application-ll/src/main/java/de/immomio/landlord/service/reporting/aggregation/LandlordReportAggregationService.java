package de.immomio.landlord.service.reporting.aggregation;

import de.immomio.data.landlord.entity.user.reporting.enums.ApplicationEventType;
import de.immomio.data.landlord.entity.user.reporting.enums.AppointmentEventType;
import de.immomio.data.landlord.entity.user.reporting.enums.PropertyEventType;
import de.immomio.data.landlord.entity.user.reporting.enums.ProposalEventType;
import de.immomio.data.landlord.entity.user.reporting.enums.ReportChart;
import de.immomio.landlord.service.reporting.data.chart.PieChartHandler;
import de.immomio.landlord.service.reporting.data.chart.SeriesChartHandler;
import de.immomio.landlord.service.reporting.extractor.series.AppointmentSlotsSeriesExtractor;
import de.immomio.landlord.service.reporting.query.ElasticsearchQueryHandler;
import de.immomio.reporting.model.chart.ReportingChartData;
import de.immomio.reporting.model.filter.ReportingFilterBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.ExistsQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.avg.AvgAggregationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Niklas Lindemann
 */
@Slf4j
@Service
public class LandlordReportAggregationService {

    private static final String PROPERTY_INDEX_PREFIX = "property-";
    private static final String APPLICATION_INDEX_PREFIX = "application-";
    private static final String APPLICATION_CURRENT_INDEX_PREFIX = "application-current-";
    private static final String APPOINTMENT_CURRENT_INDEX_PREFIX = "appointment-current-";
    private static final String PROPOSAL_INDEX_PREFIX = "proposal-";
    private static final String CHART_LABEL_PROPERTIES = "CHART_LABEL_PROPERTIES_L";
    private static final String CHART_LABEL_APPLICATIONS = "CHART_LABEL_APPLICATIONS_L";
    private static final String CHART_LABEL_PROPOSALS = "CHART_LABEL_PROPOSALS_L";
    private static final String CHART_LABEL_APPOINTMENTS = "CHART_LABEL_APPOINTMENTS_L";
    private static final String APPLICATION_PROPERTY_ATTR_PREFIX = "application";
    private static final String PROPOSAL_PROPERTY_ATTR_PREFIX = "proposal";
    private static final String APPOINTMENT_PROPERTY_ATTR_PREFIX = "appointment";
    private static final String EVENTTYPE = "eventtype";
    private static final String EVENT_TYPE_KEYWORD = "eventType.keyword";
    private static final String PORTAL = "portal";
    private static final String APPLICATION_PORTAL_KEYWORD = "application.portal.keyword";
    private static final String APPOINTMENT_DATE = "appointment.date";
    private static final String APPOINTMENT_STATE = "appointment.state";
    private static final String ACTIVE = "ACTIVE";
    private static final String HOUSEHOLD_TYPE_FIELD = "application.user.profile.householdType.keyword";
    private static final String RESIDENTS_FIELD = "application.user.profile.residents";
    private static final String PROFESSION_FIELD = "application.user.profile.profession.type.keyword";
    private static final String ANIMALS_PATH = "application.user.profile.additionalInformation.animals";
    private static final String CITY_FIELD = "application.user.address.city.keyword";
    private static final String WBS_FIELD = "application.user.profile.additionalInformation.wbs";
    private static final String ZIP_CODE_FIELD = "application.user.address.zipCode.keyword";
    private static final String INCOME_FIELD = "application.user.profile.profession.income";
    private static final String ZIP_CODE_SCRIPT = "doc['application.user.address.zipCode.keyword'].getValue().length() <= 2 ? " +
            "doc['application.user.address.zipCode.keyword'].getValue() : " +
            "doc['application.user.address.zipCode.keyword'].getValue().substring(0,2)";
    private static final String INVITEE_COUNT = "inviteeCount";
    private static final String APPOINTMENT_MAX_INVITEE_COUNT = "appointment.maxInviteeCount";
    private static final String ZIP_CODE_SUFFIX = "xxxx";

    private final SeriesChartHandler seriesChartHandler;

    private final PieChartHandler pieChartHandler;

    private final ElasticsearchQueryHandler queryHandler;

    @Autowired
    public LandlordReportAggregationService(
            SeriesChartHandler seriesChartHandler,
            PieChartHandler pieChartHandler,
            ElasticsearchQueryHandler queryHandler
    ) {
        this.seriesChartHandler = seriesChartHandler;
        this.pieChartHandler = pieChartHandler;
        this.queryHandler = queryHandler;
    }

    public ReportingChartData getPropertyCreatedSeries(ReportingFilterBean filterBean) {
        PropertyEventType eventType = PropertyEventType.PROPERTY_CREATED;
        BoolQueryBuilder baseQuery = queryHandler.getBaseDateRangeQuery(filterBean, null, eventType);
        ReportingChartData parsedDateHistogram = seriesChartHandler.getDateHistogramChartData(
                filterBean,
                PROPERTY_INDEX_PREFIX,
                baseQuery,
                ReportChart.PROPERTY_CREATED,
                CHART_LABEL_PROPERTIES);

        return parsedDateHistogram;
    }

    public ReportingChartData getPropertyPublishedSeries(ReportingFilterBean filterBean) {
        PropertyEventType eventType = PropertyEventType.PROPERTY_PUBLISHED;
        BoolQueryBuilder baseQuery = queryHandler.getBaseDateRangeQuery(filterBean, null, eventType);
        ReportingChartData parsedDateHistogram = seriesChartHandler.getDateHistogramChartData(
                filterBean,
                PROPERTY_INDEX_PREFIX,
                baseQuery,
                ReportChart.PROPERTY_PUBLISHED,
                CHART_LABEL_PROPERTIES);

        return parsedDateHistogram;
    }

    public ReportingChartData getApplicationsCountSeries(ReportingFilterBean filterBean) {
        BoolQueryBuilder baseQuery = queryHandler.getBaseDateRangeQuery(
                filterBean,
                APPLICATION_PROPERTY_ATTR_PREFIX,
                ApplicationEventType.APPLIED_POOL,
                ApplicationEventType.APPLIED_EXTERNAL);
        ReportingChartData parsedDateHistogram = seriesChartHandler.getDateHistogramChartData(
                filterBean,
                APPLICATION_INDEX_PREFIX,
                baseQuery,
                ReportChart.APPLICATIONS_TOTAL,
                CHART_LABEL_APPLICATIONS);

        return parsedDateHistogram;
    }

    public ReportingChartData getApplicationsByPortalSeries(ReportingFilterBean filterBean) {
        BoolQueryBuilder baseQuery = queryHandler.getBaseDateRangeQuery(
                filterBean,
                APPLICATION_PROPERTY_ATTR_PREFIX,
                ApplicationEventType.APPLIED_EXTERNAL);
        baseQuery.must(new ExistsQueryBuilder(APPLICATION_PORTAL_KEYWORD));
        ReportingChartData seriesChartData = seriesChartHandler.getDateHistogramChartData(
                filterBean,
                APPLICATION_INDEX_PREFIX,
                baseQuery,
                ReportChart.APPLICATIONS_BY_PORTAL,
                AggregationBuilders.terms(PORTAL).field(APPLICATION_PORTAL_KEYWORD));

        return seriesChartData;
    }

    public ReportingChartData getApplicationIntentionSeries(ReportingFilterBean filterBean) {
        BoolQueryBuilder baseQuery = queryHandler.getBaseDateRangeQuery(
                filterBean,
                APPLICATION_PROPERTY_ATTR_PREFIX,
                ApplicationEventType.INTENT_GIVEN,
                ApplicationEventType.NO_INTENT_GIVEN);
        ReportingChartData seriesChartData = seriesChartHandler.getDateHistogramChartData(
                filterBean,
                APPLICATION_INDEX_PREFIX,
                baseQuery,
                ReportChart.APPLICATIONS_INTENTIONS,
                AggregationBuilders.terms(EVENTTYPE).field(EVENT_TYPE_KEYWORD));

        return seriesChartData;
    }

    public ReportingChartData getApplicationAppointmentAcceptanceSeries(ReportingFilterBean filterBean) {
        BoolQueryBuilder baseQuery = queryHandler.getBaseDateRangeQuery(
                filterBean,
                APPLICATION_PROPERTY_ATTR_PREFIX,
                ApplicationEventType.APPLICATION_ACCEPTED,
                ApplicationEventType.APPOINTMENT_CANCELED_BY_PS,
                ApplicationEventType.APPOINTMENT_ACCEPTED);
        ReportingChartData seriesChartData = seriesChartHandler.getDateHistogramChartData(
                filterBean,
                APPLICATION_INDEX_PREFIX,
                baseQuery,
                ReportChart.APPLICATIONS_APPOINTMENT_ACCEPTANCES,
                AggregationBuilders.terms(EVENTTYPE).field(EVENT_TYPE_KEYWORD));

        return seriesChartData;
    }

    public ReportingChartData getProposalOfferedSeries(ReportingFilterBean filterBean) {
        BoolQueryBuilder baseQuery = queryHandler.getBaseDateRangeQuery(
                filterBean,
                PROPOSAL_PROPERTY_ATTR_PREFIX,
                ProposalEventType.PROPOSAL_OFFERED);
        ReportingChartData parsedDateHistogram = seriesChartHandler.getDateHistogramChartData(
                filterBean,
                PROPOSAL_INDEX_PREFIX,
                baseQuery,
                ReportChart.PROPOSAL_OFFERED,
                CHART_LABEL_PROPOSALS);

        return parsedDateHistogram;
    }

    public ReportingChartData getAppointmentOccurencesSeries(ReportingFilterBean filterBean) {
        BoolQueryBuilder baseQuery = queryHandler.getBaseDateRangeQuery(
                filterBean,
                APPOINTMENT_PROPERTY_ATTR_PREFIX,
                APPOINTMENT_DATE,
                AppointmentEventType.APPOINTMENT_UPDATED,
                AppointmentEventType.APPOINTMENT_CREATED);

        baseQuery.must(getAppointmentActiveQuery());
        ReportingChartData parsedDateHistogram = seriesChartHandler.getDateHistogramChartData(
                filterBean,
                APPOINTMENT_CURRENT_INDEX_PREFIX,
                baseQuery,
                ReportChart.APPOINTMENT_OCCURENCES,
                CHART_LABEL_APPOINTMENTS, null, APPOINTMENT_DATE, null);

        return parsedDateHistogram;
    }
    public ReportingChartData getAppointmentSlotsSeries(ReportingFilterBean filterBean) {
        BoolQueryBuilder baseQuery = queryHandler.getBaseDateRangeQuery(
                filterBean,
                APPOINTMENT_PROPERTY_ATTR_PREFIX,
                APPOINTMENT_DATE,
                AppointmentEventType.APPOINTMENT_UPDATED,
                AppointmentEventType.APPOINTMENT_CREATED);

        baseQuery.must(getAppointmentActiveQuery());
        AvgAggregationBuilder inviteeCountAggregation = AggregationBuilders
                .avg(INVITEE_COUNT)
                .field(APPOINTMENT_MAX_INVITEE_COUNT);
        ReportingChartData parsedDateHistogram = seriesChartHandler.getDateHistogramChartData(
                filterBean,
                APPOINTMENT_CURRENT_INDEX_PREFIX,
                baseQuery,
                ReportChart.APPOINTMENT_SLOTS,
                CHART_LABEL_APPOINTMENTS, inviteeCountAggregation, APPOINTMENT_DATE, new AppointmentSlotsSeriesExtractor());

        return parsedDateHistogram;
    }

    public ReportingChartData getDistributionHouseHoldTypeData(ReportingFilterBean filterBean) {
        return getBaseDistributionData(filterBean, HOUSEHOLD_TYPE_FIELD, ReportChart.HOUSEHOLD_TYPE_DISTRIBUTION);
    }

    public ReportingChartData getDistributionHouseHoldSizeData(ReportingFilterBean filterBean) {
        return getBaseDistributionData(filterBean, RESIDENTS_FIELD, ReportChart.HOUSEHOLD_SIZE_DISTRIBUTION);
    }

    public ReportingChartData getDistributionEmploymentTypeData(ReportingFilterBean filterBean) {
        return getBaseDistributionData(filterBean, PROFESSION_FIELD, ReportChart.EMPLOYMENT_TYPE_DISTRIBUTION);
    }

    public ReportingChartData getDistributionAnimalsData(ReportingFilterBean filterBean) {
        return getBaseDistributionData(filterBean, ANIMALS_PATH, ReportChart.ANIMALS_DISTRIBUTION);
    }

    public ReportingChartData getDistributionWbsData(ReportingFilterBean filterBean) {
        return getBaseDistributionData(filterBean, WBS_FIELD, ReportChart.WBS_DISTRIBUTION);
    }

    public ReportingChartData getDistributionCityData(ReportingFilterBean filterBean) {
        BoolQueryBuilder baseQuery = queryHandler.getBaseDateRangeQuery(
                filterBean, APPLICATION_PROPERTY_ATTR_PREFIX);
        ExistsQueryBuilder cityExists = QueryBuilders.existsQuery(CITY_FIELD);
        baseQuery.must(cityExists);

        return pieChartHandler.getPieChartData(
                APPLICATION_CURRENT_INDEX_PREFIX,
                baseQuery,
                CITY_FIELD,
                ReportChart.CITY_DISTRIBUTION);
    }

    public ReportingChartData getDistributionZipData(ReportingFilterBean filterBean) {
        BoolQueryBuilder baseQuery = queryHandler.getBaseDateRangeQuery(
                filterBean, APPLICATION_PROPERTY_ATTR_PREFIX);
        ExistsQueryBuilder zipExists = QueryBuilders.existsQuery(ZIP_CODE_FIELD);
        baseQuery.must(zipExists);

        ReportingChartData reportingChartData = pieChartHandler.getPieChartData(
                APPLICATION_CURRENT_INDEX_PREFIX,
                baseQuery,
                new Script(ZIP_CODE_SCRIPT),
                ReportChart.ZIP_DISTRIBUTION);

        appendZipCodeSuffixes(reportingChartData);

        return reportingChartData;
    }


    public ReportingChartData getDistributionIncomeData(ReportingFilterBean filterBean) {
        BoolQueryBuilder baseQuery = queryHandler.getBaseDateRangeQuery(
                filterBean, APPLICATION_PROPERTY_ATTR_PREFIX);
        return pieChartHandler.getRangeChartData(
                APPLICATION_CURRENT_INDEX_PREFIX,
                baseQuery,
                INCOME_FIELD,
                ReportChart.INCOME_DISTRIBUTION);
    }

    private ReportingChartData getBaseDistributionData(ReportingFilterBean filterBean, String field, ReportChart reportChart) {
        BoolQueryBuilder baseQuery = queryHandler.getBaseDateRangeQuery(
                filterBean, APPLICATION_PROPERTY_ATTR_PREFIX);
        ExistsQueryBuilder exists = QueryBuilders.existsQuery(field);
        baseQuery.must(exists);
        return pieChartHandler.getPieChartData(
                APPLICATION_CURRENT_INDEX_PREFIX,
                baseQuery, field,
                reportChart);
    }

    private MatchQueryBuilder getAppointmentActiveQuery() {
        return QueryBuilders.matchQuery(APPOINTMENT_STATE, ACTIVE);
    }

    private void appendZipCodeSuffixes(ReportingChartData reportingChartData) {
        reportingChartData.getData().forEach(pieChartValue -> {
            Map<String, Number> values = pieChartValue.getValues();
            Map<String, Number> appendedValues = new HashMap<>();
            values.forEach((key, amount) -> {
                if (StringUtils.isNotBlank(key)) {
                    appendedValues.put(NumberUtils.isCreatable(key) ? key + ZIP_CODE_SUFFIX : key, amount);
                }
            });
            pieChartValue.setValues(appendedValues);
        });
    }

}
