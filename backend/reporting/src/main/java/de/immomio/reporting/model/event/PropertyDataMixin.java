package de.immomio.reporting.model.event;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Niklas Lindemann
 */
abstract class PropertyDataMixin {
    // date field is saved as string in different date formats. ES cannot parse it correctly
    @JsonIgnore
    public abstract String getAvailableFrom();
}
