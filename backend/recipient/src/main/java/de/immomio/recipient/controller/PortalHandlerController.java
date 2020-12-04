package de.immomio.recipient.controller;

import de.immomio.recipient.service.portal.PortalEmailParseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Map;

/**
 * @author Niklas Lindemann
 */

@Slf4j
@RestController
public class PortalHandlerController {

    private static final int BASIC_PREFIX = 6;
    private static final String DD_MM_YYYY = "dd.MM.yyyy";
    private static final String STARTDATE = "startdate";
    private static final String ENDDATE = "enddate";
    private static final String AUTHORIZATION = "Authorization";
    private final PortalEmailParseService portalEmailParseService;

    @Value("${auth.basic.value}")
    private String authKey;

    @Autowired
    public PortalHandlerController(PortalEmailParseService portalEmailParseService) {
        this.portalEmailParseService = portalEmailParseService;
    }

    @RequestMapping("parse")
    public ResponseEntity triggerParsing(
            @RequestParam(STARTDATE) @DateTimeFormat(pattern = DD_MM_YYYY) LocalDate startDate,
            @RequestParam(value = ENDDATE, required = false)
            @DateTimeFormat(pattern = DD_MM_YYYY) LocalDate endDate,
            @RequestHeader(AUTHORIZATION) String authorization
    ) throws MessagingException {

        DeferredResult<ResponseEntity<?>> output = new DeferredResult<>();

        //trim BASIC
        authorization = authorization.substring(BASIC_PREFIX);
        if (!Base64.getEncoder().encodeToString(authKey.getBytes()).equals(authorization)) {
            return ResponseEntity.status(401).build();
        }

        if (endDate == null) {
            endDate = LocalDate.now();
        }

        final LocalDate finalEnddate = endDate;
        Map<String, List<String>> result = portalEmailParseService.parseEmails(startDate, finalEnddate);
        return ResponseEntity.ok().build();
    }
}
