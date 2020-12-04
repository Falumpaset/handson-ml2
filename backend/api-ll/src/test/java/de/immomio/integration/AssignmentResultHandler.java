package de.immomio.integration;

import com.jayway.jsonpath.JsonPath;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;

/**
 * @author Maik Kingma.
 */
public class AssignmentResultHandler implements ResultHandler {

    private final JsonPath jsonPath;

    private final AssignmentResult assignmentResult;

    protected AssignmentResultHandler(JsonPath jsonPath, AssignmentResult assignmentResult) {
        this.jsonPath = jsonPath;
        this.assignmentResult = assignmentResult;
    }

    public static ResultHandler assignTo(String path, AssignmentResult assignmentResult) {
        return new AssignmentResultHandler(JsonPath.compile(path), assignmentResult);
    }

    @Override
    public void handle(MvcResult result) throws Exception {
        String resultString = result.getResponse().getContentAsString();

        assignmentResult.setValue(jsonPath.read(resultString));
    }
}