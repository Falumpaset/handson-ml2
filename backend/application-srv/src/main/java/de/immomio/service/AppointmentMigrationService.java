package de.immomio.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.immomio.data.landlord.bean.user.AgentInfo;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.shared.entity.appointment.Appointment;
import de.immomio.model.repository.service.landlord.customer.LandlordCustomerRepository;
import de.immomio.model.repository.service.landlord.customer.user.LandlordUserRepository;
import de.immomio.model.repository.service.shared.appointment.AppointmentRepository;
import de.immomio.reporting.model.event.AbstractEvent;
import de.immomio.reporting.model.event.customer.AppointmentEvent;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author Niklas Lindemann
 */

@Slf4j
@Service
public class AppointmentMigrationService {

    private RestHighLevelClient restHighLevelClient;

    private final AppointmentRepository appointmentRepository;
    private final LandlordCustomerRepository customerRepository;
    private final LandlordUserRepository landlordUserRepository;

    @Autowired
    public AppointmentMigrationService(RestHighLevelClient restHighLevelClient,
            AppointmentRepository appointmentRepository,
            LandlordCustomerRepository customerRepository,
            LandlordUserRepository landlordUserRepository) {
        this.restHighLevelClient = restHighLevelClient;
        this.appointmentRepository = appointmentRepository;
        this.customerRepository = customerRepository;
        this.landlordUserRepository = landlordUserRepository;
    }


    public void migrateAppointmentCreators() {
        List<LandlordCustomer> customers = customerRepository.findAll();
        AtomicInteger atomicInteger = new AtomicInteger(1);
        List<LandlordCustomer> filteredCustomers = customers.stream()
                .filter(customer -> customer.getActiveProduct() != null).collect(Collectors.toList());
        filteredCustomers
                .forEach(customer -> {
                    log.info("PROCESSING CUSTOMER " + atomicInteger.getAndIncrement() + " of " + filteredCustomers.size());
                    SearchRequest searchRequest = new SearchRequest("appointment-" + customer.getId());
                    List<AppointmentEvent> appointmentEvents = getData(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("eventType.keyword", "APPOINTMENT_CREATED")), searchRequest, AppointmentEvent.class);

                    List<Appointment> appointments = appointmentRepository.findByCustomer(customer);
                    appointments.forEach(appointment -> {
                        appointmentEvents.stream()
                                .filter(appointmentEvent -> Objects.equals(appointmentEvent.getAppointment().getId(), appointment.getId()))
                                .findFirst()
                                .or(() -> appointmentEvents.stream().filter(appointmentEvent -> appointmentEvent.getAppointment().getProperty().getId().equals(appointment.getProperty().getId()) && appointmentEvent.getAppointment().getDate().compareTo(appointment.getDate()) == 0).findFirst())
                                .or(() -> appointmentEvents.stream().filter(appointmentEvent -> appointmentEvent.getAppointment().getProperty().getId().equals(appointment.getProperty().getId())).findFirst())
                                .flatMap(appointmentEvent -> landlordUserRepository.findById(appointmentEvent.getEditor().getId())
                                        .filter(user -> user.getCustomer().equals(customer)))
                                .ifPresent(user -> appointment.setAgentInfo(new AgentInfo(user)));

                    });
                    appointmentRepository.saveAll(appointments);

                });


    }


    protected SearchHits performQuery(SearchRequest request, QueryBuilder queryBuilder, int size) throws IOException {
        request.source(new SearchSourceBuilder().query(queryBuilder).size(size));
        try {
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);

            return response.getHits();
        } catch (ElasticsearchException e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }


    protected <T extends AbstractEvent> List<T> getData(QueryBuilder baseQuery, SearchRequest request, Class<T> clazz) {
        try {
            SearchHits searchHits = performQuery(request, baseQuery, 10000);
            return Arrays.stream(searchHits.getHits()).map(hit -> {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                try {
                    T t = objectMapper.readValue(hit.getSourceAsString(), clazz);
                    t.setDocumentId(hit.getId());
                    return t;
                } catch (JsonProcessingException e) {
                    log.error(e.getMessage(), e);
                    return null;
                }
            }).filter(Objects::nonNull).collect(Collectors.toList());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Collections.emptyList();
    }
}
