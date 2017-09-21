package de.florianschmitt.rest

import com.github.springtestdbunit.annotation.DatabaseOperation
import com.github.springtestdbunit.annotation.DatabaseSetup
import com.github.springtestdbunit.annotation.DatabaseSetups
import de.florianschmitt.model.rest.EPaymentDTO
import de.florianschmitt.rest.base.BaseRestTest
import de.florianschmitt.rest.base.DBUnitData
import org.junit.Test
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpStatus
import org.springframework.web.client.getForEntity
import org.springframework.web.client.postForEntity
import java.math.BigDecimal
import kotlin.test.assertEquals

class PaymentControllerTest : BaseRestTest() {

    @Test
    @DatabaseSetups(
            DatabaseSetup(value = DBUnitData.BASE),
            DatabaseSetup(value = DBUnitData.ADMIN_USER, type = DatabaseOperation.REFRESH),
            DatabaseSetup(value = DBUnitData.REQUEST_FINISHED, type = DatabaseOperation.REFRESH))
    fun `get all should be empty`() {
        val response = restTemplate.getForEntity<Any>(buildUrl("/admin/payment/all"))

        val map = response.body as Map<*, *>
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(0, map["numberOfElements"] as Int)
    }

    @Test
    @DatabaseSetups(
            DatabaseSetup(value = DBUnitData.BASE),
            DatabaseSetup(value = DBUnitData.ADMIN_USER, type = DatabaseOperation.REFRESH),
            DatabaseSetup(value = DBUnitData.REQUEST_FINISHED, type = DatabaseOperation.REFRESH))
    fun `place payment should work`() {
        val dto = EPaymentDTO()
        dto.paymentReceived = BigDecimal(20)
        dto.requestId = "ident3"

        val response = restTemplate.postForEntity<Any>(buildUrl("/admin/payment/placePayment"), dto)
        assertEquals(HttpStatus.OK, response.statusCode)
    }

    @Test
    @DatabaseSetups(
            DatabaseSetup(value = DBUnitData.BASE),
            DatabaseSetup(value = DBUnitData.ADMIN_USER, type = DatabaseOperation.REFRESH),
            DatabaseSetup(value = DBUnitData.REQUEST_FINISHED, type = DatabaseOperation.REFRESH))
    fun `payment for request should be empty`() {
        val response = restTemplate.getForEntity<Any>(buildUrl("/admin/payment/forRequest?requestIdentifier=" + "ident3"))
        val map = response.body as Map<*, *>
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(0, map["numberOfElements"] as Int)
    }

    @Test
    @DatabaseSetups(
            DatabaseSetup(value = DBUnitData.BASE),
            DatabaseSetup(value = DBUnitData.ADMIN_USER, type = DatabaseOperation.REFRESH),
            DatabaseSetup(value = DBUnitData.REQUEST_FINISHED, type = DatabaseOperation.REFRESH))
    fun `place payment, afterward forRequest should return payment`() {
        val dto = EPaymentDTO()
        dto.paymentReceived = BigDecimal(20)
        dto.requestId = "ident3"

        var response = restTemplate.postForEntity<Any>(buildUrl("/admin/payment/placePayment"), dto)
        assertEquals(HttpStatus.OK, response.statusCode)

        response = restTemplate.getForEntity(buildUrl("/admin/payment/forRequest?requestIdentifier=" + "ident3"))
        val map = response.body as Map<*, *>
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(1, map["numberOfElements"] as Int)

        val rows: List<*> = map["content"] as List<*>
        val firstRow = rows[0] as Map<*, *>

        assertEquals("user, admin", firstRow["paymentBookedBy"])
        assertEquals(20.0, firstRow["paymentReceived"])
        assertEquals("ident3", firstRow["requestId"])
    }

    override fun customize(restBuilder: RestTemplateBuilder): RestTemplateBuilder {
        return restBuilder.basicAuthorization("admin@connection.de", "admin123")
    }
}