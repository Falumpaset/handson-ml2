package de.immomio.integration;

import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.HeaderResultMatchers;

import static org.springframework.test.util.AssertionErrors.assertTrue;

/**
 * @author Maik Kingma.
 */
public class CustomHeaderResultMatcher extends HeaderResultMatchers {

    public static CustomHeaderResultMatcher header() {
        return new CustomHeaderResultMatcher();
    }

    public ResultMatcher doesExist(final String name) {
        return result -> assertTrue("Response should contain header '" + name + "'",
                result.getResponse().containsHeader(name));
    }
}
