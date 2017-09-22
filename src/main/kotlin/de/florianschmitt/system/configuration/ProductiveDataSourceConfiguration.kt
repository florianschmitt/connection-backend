package de.florianschmitt.system.configuration

import com.zaxxer.hikari.HikariDataSource
import de.florianschmitt.system.util.ProductiveProfile
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import javax.sql.DataSource
import java.net.URI
import java.net.URISyntaxException

@Configuration
@ProductiveProfile
class ProductiveDataSourceConfiguration {

    @Bean
    @Throws(URISyntaxException::class)
    fun dataSource(): DataSource {
        val dbUri = URI(System.getenv("DATABASE_URL"))

        val username = dbUri.userInfo.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
        val password = dbUri.userInfo.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
        val dbUrl = "jdbc:postgresql://" + dbUri.host + ':' + dbUri.port + dbUri.path

        val basicDataSource = HikariDataSource()
        basicDataSource.jdbcUrl = dbUrl
        basicDataSource.username = username
        basicDataSource.password = password

        return basicDataSource
    }
}
