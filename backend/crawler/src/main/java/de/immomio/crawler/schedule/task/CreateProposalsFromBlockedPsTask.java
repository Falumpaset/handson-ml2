package de.immomio.crawler.schedule.task;

import de.immomio.crawler.schedule.task.base.BaseTask;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.messaging.container.user.PSUserProfileMessageContainer;
import de.immomio.messaging.converter.MessagingDefaultJackson2Serializer;
import de.immomio.model.repository.core.propertysearcher.searchprofile.BasePropertySearcherSearchProfileRepository;
import de.immomio.model.repository.core.propertysearcher.user.BasePropertySearcherUserProfileRepository;
import de.immomio.model.repository.core.propertysearcher.user.BasePropertySearcherUserRepository;
import org.joda.time.LocalDateTime;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

import static de.immomio.messaging.config.QueueConfigUtils.PropertySearcherConfig.EXCHANGE_NAME;
import static de.immomio.messaging.config.QueueConfigUtils.PropertySearcherConfig.ROUTING_KEY;

/**
 * @author Niklas Lindemann
 */

@Component
public class CreateProposalsFromBlockedPsTask extends BaseTask {

    private static final int ONE = 1;
    private BasePropertySearcherUserProfileRepository userProfileRepository;

    private BasePropertySearcherSearchProfileRepository searchProfileRepository;

    private RabbitTemplate rabbitTemplate;

    @Value("${proposal.createThresholdWeeks}")
    private Integer proposalThreshold;

    @Autowired
    public CreateProposalsFromBlockedPsTask(
            BasePropertySearcherUserProfileRepository userProfileRepository,
            BasePropertySearcherSearchProfileRepository searchProfileRepository,
            RabbitTemplate rabbitTemplate
    ) {
        this.userProfileRepository = userProfileRepository;
        this.searchProfileRepository = searchProfileRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public boolean run() {
        LocalDateTime proposalTimestamp = LocalDateTime.now().minusWeeks(proposalThreshold);
        Date dateFrom = proposalTimestamp.minusDays(ONE).toDate();
        Date dateTo = proposalTimestamp.toDate();

        List<PropertySearcherUserProfile> psUsers =
                userProfileRepository.findAllByCreatedBetween(dateFrom, dateTo);

        psUsers.stream()
                .map(PSUserProfileMessageContainer::new)
                .forEach(container -> {
                    rabbitTemplate.setMessageConverter(MessagingDefaultJackson2Serializer.jackson2JsonMessageConverter());
                    rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, container);
                });

        return true;
    }
}
