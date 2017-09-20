package de.florianschmitt.rest.base;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import com.github.springtestdbunit.bean.DatabaseConfigBean;
import com.github.springtestdbunit.bean.DatabaseDataSourceConnectionFactoryBean;

@Configuration
@Profile("test")
public class TestingDataSourceConfiguration {

    public static final String DATABASE_CONNECTION_NAME = "dbUnitDatabaseConnection";

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder() //
                .setType(EmbeddedDatabaseType.H2) //
                .build();//
    }

    @Bean
    public DatabaseConfigBean dbUnitDatabaseConfig() {
        DatabaseConfigBean result = new DatabaseConfigBean();
        result.setAllowEmptyFields(true);
        return result;
    }

    @Bean
    public DatabaseDataSourceConnectionFactoryBean dbUnitDatabaseConnection() {
        DatabaseDataSourceConnectionFactoryBean result = new DatabaseDataSourceConnectionFactoryBean();
        result.setDatabaseConfig(dbUnitDatabaseConfig());
        result.setDataSource(dataSource());
        return result;
    }
}
