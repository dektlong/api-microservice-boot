package backend.connectors;

import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@Profile("mysql")
public class MySQLTemplate extends AbstractCloudConfig {

    @Bean("mybean")
    public DataSource dataSource() {
        return connectionFactory().dataSource();
    }

}
