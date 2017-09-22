package de.florianschmitt.rest

import com.github.springtestdbunit.annotation.DatabaseOperation
import com.github.springtestdbunit.annotation.DatabaseSetup
import com.github.springtestdbunit.annotation.DatabaseSetups
import de.florianschmitt.model.rest.ERequestDTO
import de.florianschmitt.base.BaseRestTest
import de.florianschmitt.base.DBUnitData
import org.junit.Test
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.web.client.getForEntity
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RequestAdminControllerTest : BaseRestTest() {

    @Test
    @DatabaseSetups(
            DatabaseSetup(value = DBUnitData.BASE),
            DatabaseSetup(value = DBUnitData.ADMIN_USER, type = DatabaseOperation.REFRESH),
            DatabaseSetup(value = DBUnitData.REQUEST_FINISHED, type = DatabaseOperation.REFRESH))
    fun `get single request should return request`() {
        val response = restTemplate.getForEntity<ERequestDTO>(buildUrl("/admin/request/get/ident3"))
        assertTrue(response.statusCode.is2xxSuccessful)
        assertEquals("ident3", response.body.requestIdentifier)
    }


    @Test
    @DatabaseSetups(
            DatabaseSetup(value = DBUnitData.BASE),
            DatabaseSetup(value = DBUnitData.ADMIN_USER, type = DatabaseOperation.REFRESH),
            DatabaseSetup(value = DBUnitData.REQUEST_FINISHED, type = DatabaseOperation.REFRESH))
    fun `get answers should return voucher answers`() {
        val response = restTemplate.getForEntity<Any>(buildUrl("/admin/request/getAnswers/ident3"))
        assertTrue(response.statusCode.is2xxSuccessful)

        val rows = response.body as Collection<*>
        val row1 = rows.first() as Map<*, *>

        assertEquals(1, rows.size)
        assertEquals("voucherId1", row1["identifier"])
        assertEquals(null, row1["answer"])
    }

    @Test
    @DatabaseSetups(
            DatabaseSetup(value = DBUnitData.BASE),
            DatabaseSetup(value = DBUnitData.ADMIN_USER, type = DatabaseOperation.REFRESH),
            DatabaseSetup(value = DBUnitData.REQUEST_FINISHED, type = DatabaseOperation.REFRESH))
    fun `all should return all`() {
        val response = restTemplate.getForEntity<Any>(buildUrl("/admin/request/all"))
        assertTrue(response.statusCode.is2xxSuccessful)

        val map = response.body as Map<*, *>
        assertEquals(1, map["numberOfElements"] as Int)

        val rows: List<*> = map["content"] as List<*>
        val firstRow = rows[0] as Map<*, *>

        assertEquals("ident3", firstRow["requestIdentifier"])
        assertEquals("abgeschlossen", firstRow["state"])
    }

    @Test
    @DatabaseSetups(
            DatabaseSetup(value = DBUnitData.BASE),
            DatabaseSetup(value = DBUnitData.ADMIN_USER, type = DatabaseOperation.REFRESH),
            DatabaseSetup(value = DBUnitData.REQUEST_FINISHED, type = DatabaseOperation.REFRESH))
    fun `not payed`() {
        val response = restTemplate.getForEntity<Any>(buildUrl("/admin/request/notPayed"))
        assertTrue(response.statusCode.is2xxSuccessful)

        val map = response.body as Map<*, *>
        assertEquals(1, map["numberOfElements"] as Int)

        val rows: List<*> = map["content"] as List<*>
        val firstRow = rows[0] as Map<*, *>

        assertEquals("ident3", firstRow["requestIdentifier"])
        assertEquals("abgeschlossen", firstRow["state"])
    }

    override fun customize(restBuilder: RestTemplateBuilder): RestTemplateBuilder {
        return restBuilder.basicAuthorization("admin@connection.de", "admin123")
    }
}