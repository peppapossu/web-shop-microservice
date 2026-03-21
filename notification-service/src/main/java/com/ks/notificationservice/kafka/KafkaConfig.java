package com.ks.notificationservice.kafka;

import com.ks.avro.order.OrderCreatedEvent;
import org.apache.kafka.common.errors.SerializationException;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.DeserializationException;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {


    @Bean
    @SuppressWarnings({"unchecked"})
    ConcurrentKafkaListenerContainerFactory<String, byte[]> kafkaListenerContainerFactory(
            ConcurrentKafkaListenerContainerFactoryConfigurer configurer,
            ConsumerFactory<String, byte[]> consumerFactory, DefaultErrorHandler errorHandler
    ) {
        ConcurrentKafkaListenerContainerFactory<String, byte[]> factory =
                new ConcurrentKafkaListenerContainerFactory<>();


        factory.setConsumerFactory(consumerFactory);
//        factory.getContainerProperties()
//                .setAckMode(ContainerProperties.AckMode.MANUAL);
        factory.setCommonErrorHandler(errorHandler);

        configurer.configure(
                (ConcurrentKafkaListenerContainerFactory) factory,
                (ConsumerFactory) consumerFactory);

        return factory;
    }

    @Bean
    DefaultErrorHandler kafkaErrorHandler(KafkaTemplate<Object, Object> kafkaTemplate) {

        // можно отправлять в DLT
        DeadLetterPublishingRecoverer recoverer =
                new DeadLetterPublishingRecoverer(kafkaTemplate);

        DefaultErrorHandler errorHandler =
                new DefaultErrorHandler(recoverer, new FixedBackOff(0L, 0));

        // ❗ коммитить offset при ошибке
        errorHandler.setAckAfterHandle(true);

        // ❗ десериализация НЕ должна ретраиться
        errorHandler.addNotRetryableExceptions(
                SerializationException.class,
                DeserializationException.class
        );

        return errorHandler;
    }


}
