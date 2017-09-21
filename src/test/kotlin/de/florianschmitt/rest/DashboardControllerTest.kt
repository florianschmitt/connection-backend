package de.florianschmitt.rest

import com.github.springtestdbunit.annotation.DatabaseOperation
import com.github.springtestdbunit.annotation.DatabaseSetup
import com.github.springtestdbunit.annotation.DatabaseSetups
import de.florianschmitt.model.rest.EDashboardDTO
import de.florianschmitt.rest.base.BaseRestTest
import de.florianschmitt.rest.base.DBUnitData
import org.junit.Test
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpStatus
import kotlin.test.assertEquals

class DashboardControllerTest : BaseRestTest() {

    @Test
    @DatabaseSetups(
            DatabaseSetup(value = DBUnitData.BASE),
            DatabaseSetup(value = DBUnitData.ADMIN_USER, type = DatabaseOperation.REFRESH),
            DatabaseSetup(value = DBUnitData.REQUEST_FINISHED, type = DatabaseOperation.REFRESH))
    fun `dashboard info with 1 finished request`() {
        val response = restTemplate.getForEntity(buildUrl("/admin/dashboard/info"), EDashboardDTO::class.java)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(1, response.body.finishedRequests)
        assertEquals(0, response.body.acceptedRequests)
        assertEquals(0, response.body.canceledRequests)
        assertEquals(0, response.body.openRequests)
    }

    @Test
    @DatabaseSetups(
            DatabaseSetup(value = DBUnitData.BASE),
            DatabaseSetup(value = DBUnitData.ADMIN_USER, type = DatabaseOperation.REFRESH),
            DatabaseSetup(value = DBUnitData.REQUEST_OPEN, type = DatabaseOperation.REFRESH))
    fun `dashboard info with 1 open request`() {
        val response = restTemplate.getForEntity(buildUrl("/admin/dashboard/info"), EDashboardDTO::class.java)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(0, response.body.finishedRequests)
        assertEquals(0, response.body.acceptedRequests)
        assertEquals(0, response.body.canceledRequests)
        assertEquals(1, response.body.openRequests)
    }

    @Test
    @DatabaseSetups(
            DatabaseSetup(value = DBUnitData.BASE),
            DatabaseSetup(value = DBUnitData.ADMIN_USER, type = DatabaseOperation.REFRESH),
            DatabaseSetup(value = DBUnitData.REQUEST_CANCELED, type = DatabaseOperation.REFRESH))
    fun `dashboard info with 1 canceled request`() {
        val response = restTemplate.getForEntity(buildUrl("/admin/dashboard/info"), EDashboardDTO::class.java)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(0, response.body.finishedRequests)
        assertEquals(0, response.body.acceptedRequests)
        assertEquals(1, response.body.canceledRequests)
        assertEquals(0, response.body.openRequests)
    }


    override fun customize(restBuilder: RestTemplateBuilder): RestTemplateBuilder {
        return restBuilder.basicAuthorization("admin@connection.de", "admin123")
    }
}