package io.storebackend.api.data;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.RabbitMQFactory;
import org.springframework.data.mongodb.core.RabbitMQTemplate;

@Configuration
@Profile("rabbitmq")
public class RabbitMQTemplate {

    @Bean("mybean")
    public RabbitMQTemplate rabbitmqTemplate(RabbitMQFactory rabbitmqFactory) {
        return new RabbitMQTemplate(rabbitmqFactory);
    }

}
