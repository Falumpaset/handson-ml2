/**
 *
 */
package de.immomio.controller.property;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Johannes Hiemer.
 */
public class SearchQuery {

    private Map<String, Object> search = new HashMap<>();

    public Map<String, Object> getSearch() {
        return search;
    }

    public void setSearch(Map<String, Object> search) {
        this.search = search;
    }

}
