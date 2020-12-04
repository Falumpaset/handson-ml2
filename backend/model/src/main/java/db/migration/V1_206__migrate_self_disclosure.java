package db.migration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import java.util.Arrays;

/**
 * @author Niklas Lindemann
 */
public class V1_206__migrate_self_disclosure extends BaseJavaMigration {

    @Override
    public void migrate(Context context) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        JdbcTemplate jdbcTemplate = new JdbcTemplate(new SingleConnectionDataSource(context.getConnection(), true));
        jdbcTemplate
                .query("SELECT * FROM shared.self_disclosure_question WHERE self_disclosure_id != 1000", resultSet -> {
                    SDQuestion[] subQuestions = new SDQuestion[]{};
                    try {
                        subQuestions = objectMapper.readValue(resultSet.getString("sub_questions"), SDQuestion[].class);
                    } catch (Exception e) {
                    }
                    processSdQuestion(new SDQuestion(resultSet.getString("title"), resultSet.getBoolean("mandatory"), resultSet.getBoolean("hidden"), resultSet.getLong("self_disclosure_id"), subQuestions), jdbcTemplate);
                });

        jdbcTemplate.execute("DELETE FROM shared.self_disclosure_question where self_disclosure_id != 1000");
        jdbcTemplate.execute("ALTER TABLE shared.self_disclosure_question DROP COLUMN self_disclosure_id CASCADE ");
        jdbcTemplate.execute("ALTER TABLE shared.self_disclosure_question DROP COLUMN desired_responses CASCADE");
        jdbcTemplate.execute("ALTER TABLE shared.self_disclosure_question DROP COLUMN sub_questions CASCADE");
        jdbcTemplate.execute("DELETE FROM shared.self_disclosure where customer_id is null");
    }

    private void processSdQuestion(SDQuestion sdQuestion, JdbcTemplate jdbcTemplate) {
        Long nextId = jdbcTemplate.queryForObject("Select nextval('dictionary_seq') from public.dictionary_seq", Long.class);
        Long questionId = jdbcTemplate.queryForObject(String.format("SELECT id FROM shared.self_disclosure_question WHERE self_disclosure_id = 1000 AND title = '%s'", sdQuestion.getTitle()), Long.class);
        jdbcTemplate.execute(String.format("INSERT INTO shared.self_disclosure_question_configuration VALUES (%s, %s, %s, %s,%s, now(), now() )", nextId, sdQuestion.getSdId(), questionId, sdQuestion.isHidden(), sdQuestion.isMandatory()));

        Arrays.stream(sdQuestion.getSubQuestions()).forEach(subQuestion -> {
            Long subQuestionId = jdbcTemplate.queryForObject(String.format("SELECT id FROM shared.self_disclosure_sub_question WHERE  title = '%s'", subQuestion.getTitle()), Long.class);
            jdbcTemplate.execute(String.format("INSERT INTO shared.self_disclosure_sub_question_configuration VALUES ((Select nextval('dictionary_seq') from public.dictionary_seq), %s, %s, %s, %s, now(), now())", nextId, subQuestionId, subQuestion.isHidden(), subQuestion.isMandatory()));
        });

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static final class SDQuestion {
        private String title;
        private boolean mandatory;
        private boolean hidden;
        private Long sdId;
        private SDQuestion[] subQuestions;
    }

}
