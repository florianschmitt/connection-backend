package de.florianschmitt.system.configuration

import com.zaxxer.hikari.HikariDataSource
import de.florianschmitt.system.util.ProductiveProfile
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.net.URI
import java.net.URISyntaxException
import javax.sql.DataSource

@Configuration
@ProductiveProfile
class ProductiveDataSourceConfiguration {

    @Bean
    @Throws(URISyntaxException::class)
    fun dataSource(): DataSource {
        val dbUri = URI(System.getenv("DATABASE_URL"))

        val username = dbUri.userInfo.split(":")[0]
        val password = dbUri.userInfo.split(":")[1]

        val dataSource = HikariDataSource()
        dataSource.jdbcUrl = "jdbc:postgresql://${dbUri.host}:${dbUri.port}${dbUri.path}"
        dataSource.username = username
        dataSource.password = password

        return dataSource
    }
}
