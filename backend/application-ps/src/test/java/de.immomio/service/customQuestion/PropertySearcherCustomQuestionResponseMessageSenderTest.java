package de.immomio.service.customQuestion;

import de.immomio.AbstractTest;
import de.immomio.messaging.container.customQuestion.CustomQuestionResponseContainer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

/**
 * @author Freddy Sawma
 */

@ExtendWith(MockitoExtension.class)
public class PropertySearcherCustomQuestionResponseMessageSenderTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Spy
    @InjectMocks
    private PropertySearcherCustomQuestionResponseMessageSender responseMessageSender;

    @Test
    public void sendCustomQuestionResponseMessage() {
        CustomQuestionResponseContainer container = new CustomQuestionResponseContainer();
        responseMessageSender.sendCustomQuestionResponseMessage(container);

        verify(rabbitTemplate, times(1))
                .setMessageConverter(any(Jackson2JsonMessageConverter.class));
        verify(rabbitTemplate, times(1))
                .convertAndSend(anyString(), anyString(), any(CustomQuestionResponseContainer.class));
    }
}