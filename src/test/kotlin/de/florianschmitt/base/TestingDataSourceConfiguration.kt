package de.florianschmitt.base

import javax.sql.DataSource

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType

import com.github.springtestdbunit.bean.DatabaseConfigBean
import com.github.springtestdbunit.bean.DatabaseDataSourceConnectionFactoryBean

@Configuration
@Profile("test")
class TestingDataSourceConfiguration {

    @Bean
    fun dataSource(): DataSource {
        return EmbeddedDatabaseBuilder() //
                .setType(EmbeddedDatabaseType.H2) //
                .build()//
    }

    @Bean
    fun dbUnitDatabaseConfig(): DatabaseConfigBean {
        val result = DatabaseConfigBean()
        result.allowEmptyFields = true
        return result
    }

    @Bean
    fun dbUnitDatabaseConnection(): DatabaseDataSourceConnectionFactoryBean {
        val result = DatabaseDataSourceConnectionFactoryBean()
        result.setDatabaseConfig(dbUnitDatabaseConfig())
        result.setDataSource(dataSource())
        return result
    }

    companion object {

        const val DATABASE_CONNECTION_NAME = "dbUnitDatabaseConnection"
    }
}
