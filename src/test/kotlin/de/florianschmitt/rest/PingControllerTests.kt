package de.florianschmitt.rest

import com.github.springtestdbunit.annotation.DatabaseOperation
import com.github.springtestdbunit.annotation.DatabaseSetup
import com.github.springtestdbunit.annotation.DatabaseSetups
import de.florianschmitt.rest.base.BaseRestTest
import de.florianschmitt.rest.base.DBUnitData
import org.junit.Test
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException
import kotlin.test.assertEquals
import kotlin.test.fail

class PingControllerAuthenticatedTest : BaseRestTest() {

    @Test
    @DatabaseSetups(
            DatabaseSetup(value = DBUnitData.BASE),
            DatabaseSetup(value = DBUnitData.ADMIN_USER, type = DatabaseOperation.REFRESH))
    fun `ping should work with authentication`() {
        val response = restTemplate.getForEntity(buildUrl("/admin/ping"), Any::class.java)
        assertEquals(HttpStatus.OK, response.statusCode)
    }

    override fun customize(restBuilder: RestTemplateBuilder): RestTemplateBuilder {
        return restBuilder.basicAuthorization("admin@connection.de", "admin123")
    }
}

class PingControllerNotAuthenticatedTest : BaseRestTest() {

    @Test
    @DatabaseSetups(
            DatabaseSetup(value = DBUnitData.BASE),
            DatabaseSetup(value = DBUnitData.ADMIN_USER, type = DatabaseOperation.REFRESH))
    fun `ping should not work without authentication`() {
        try {
            restTemplate.getForEntity(buildUrl("/admin/ping"), Any::class.java)
            fail()
        } catch (e: HttpClientErrorException) {
            assertEquals(HttpStatus.UNAUTHORIZED, e.statusCode)
        }
    }
}

class PingControllerWrongAuthenticationTest : BaseRestTest() {

    @Test
    @DatabaseSetups(
            DatabaseSetup(value = DBUnitData.BASE),
            DatabaseSetup(value = DBUnitData.ADMIN_USER, type = DatabaseOperation.REFRESH))
    fun `ping should not work with wrong authentication`() {
        try {
            restTemplate.getForEntity(buildUrl("/admin/ping"), Any::class.java)
            fail()
        } catch (e: HttpClientErrorException) {
            assertEquals(HttpStatus.UNAUTHORIZED, e.statusCode)
        }
    }

    override fun customize(restBuilder: RestTemplateBuilder): RestTemplateBuilder {
        return restBuilder.basicAuthorization("user@asdf.de", "wrong")
    }
}