package de.immomio.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.context.annotation.Configuration;

/**
 * @author Niklas Lindemann
 */

@Configuration
public class SrvRestMvcConfiguration extends DefaultRestMvcConfiguration {

    @Override
    public void configureJacksonObjectMapper(ObjectMapper objectMapper) {
        SimpleFilterProvider filterProvider = new SimpleFilterProvider();
        filterProvider.addFilter("propertyFilter", SimpleBeanPropertyFilter.serializeAll());
        objectMapper.setFilterProvider(filterProvider);
        super.configureJacksonObjectMapper(objectMapper);
    }
}
