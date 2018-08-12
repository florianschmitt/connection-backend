package de.florianschmitt.base

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener
import com.github.springtestdbunit.annotation.DbUnitConfiguration
import com.github.springtestdbunit.dataset.ReplacementDataSetLoader
import org.apache.http.config.RegistryBuilder
import org.apache.http.conn.socket.ConnectionSocketFactory
import org.apache.http.conn.socket.PlainConnectionSocketFactory
import org.apache.http.conn.ssl.NoopHostnameVerifier
import org.apache.http.conn.ssl.SSLConnectionSocketFactory
import org.apache.http.conn.ssl.TrustSelfSignedStrategy
import org.apache.http.impl.client.HttpClients
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager
import org.apache.http.ssl.SSLContextBuilder
import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener
import org.springframework.web.client.RestTemplate
import java.io.IOException
import java.security.KeyManagementException
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException

@RunWith(SpringRunner::class)
@SpringJUnitConfig
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@TestExecutionListeners(DependencyInjectionTestExecutionListener::class, TransactionDbUnitTestExecutionListener::class)
@DbUnitConfiguration(databaseOperationLookup = DBOperationLookup::class, databaseConnection = arrayOf(TestingDataSourceConfiguration.DATABASE_CONNECTION_NAME), dataSetLoader = ReplacementDataSetLoader::class)
abstract class BaseRestTest {
    protected lateinit var restTemplate: RestTemplate

    @LocalServerPort
    private val port: Int = 0

    @Before
    @Throws(KeyManagementException::class, NoSuchAlgorithmException::class, KeyStoreException::class)
    fun initRestTemplate() {
        val builder = SSLContextBuilder()
        builder.loadTrustMaterial(null, TrustSelfSignedStrategy())
        val sslConnectionSocketFactory = SSLConnectionSocketFactory(builder.build(),
                NoopHostnameVerifier.INSTANCE)
        val registry = RegistryBuilder.create<ConnectionSocketFactory>()
                .register("http", PlainConnectionSocketFactory()).register("https", sslConnectionSocketFactory)
                .build()

        val cm = PoolingHttpClientConnectionManager(registry)
        cm.maxTotal = 100
        val httpClient = HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory)
                .setConnectionManager(cm).build()

        val requestFactory = HttpComponentsClientHttpRequestFactory(httpClient)

        var restBuilder = RestTemplateBuilder().requestFactory { requestFactory }
        restBuilder = customize(restBuilder)
        restTemplate = restBuilder.build()
    }

    protected fun fromJsonStringToMap(json: String): Map<String, String> {
        val mapper = ObjectMapper()
        return try {
            mapper.readValue(json, object : TypeReference<Map<String, String>>() {
            })
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    protected open fun customize(restBuilder: RestTemplateBuilder): RestTemplateBuilder {
        return restBuilder
    }

    protected fun buildUrl(path: String): String {
        return String.format("https://localhost:%s/%s", port, path)
    }
}
