package io.storebackend.api.data;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@Profile("mongodb")
public class MongoTemplate {

    @Bean("mybean")
    public MongoTemplate mongoTemplate(MongoDbFactory mongoDbFactory) {
        return new MongoTemplate(mongoDbFactory);
    }

}
