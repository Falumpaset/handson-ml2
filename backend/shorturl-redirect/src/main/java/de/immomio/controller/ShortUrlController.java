/**
 *
 */
package de.immomio.controller;

import de.immomio.service.shortUrl.ShortUrlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URL;

/**
 * @author Bastian Bliemeister.
 */

@Slf4j
@Controller
public class ShortUrlController {

    @Autowired
    private ShortUrlService shortUrlService;

    @GetMapping(value = {"", "/**", "/{externalId}"})
    public String redirect(@PathVariable(required = false, name = "externalId") String externalId,
                           HttpServletRequest request, HttpServletResponse response) {

        URL redirect = shortUrlService.getRedirect(externalId);

        return "redirect:" + redirect;
    }
}
