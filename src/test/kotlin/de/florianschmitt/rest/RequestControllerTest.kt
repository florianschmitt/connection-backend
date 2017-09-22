package de.florianschmitt.rest

import com.github.springtestdbunit.annotation.DatabaseOperation
import com.github.springtestdbunit.annotation.DatabaseSetup
import com.github.springtestdbunit.annotation.DatabaseSetups
import com.google.common.collect.Sets
import de.florianschmitt.base.BaseRestTest
import de.florianschmitt.base.DBUnitData
import de.florianschmitt.model.entities.ERequestStateEnum
import de.florianschmitt.model.rest.ERequestDTO
import de.florianschmitt.repository.RequestRepository
import de.florianschmitt.repository.VoucherRepository
import de.florianschmitt.rest.exception.RequestWasCanceledException
import org.junit.Ignore
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestClientException
import org.springframework.web.client.getForEntity
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class RequestControllerTest : BaseRestTest() {

    @Autowired
    private lateinit var requestRepository: RequestRepository

    @Autowired
    private lateinit var voucherRepository: VoucherRepository

    @Test
    @DatabaseSetup(value = DBUnitData.BASE, type = DatabaseOperation.CLEAN_INSERT)
    fun testPlaceRequest() {
        assertEquals(0L, requestRepository.count())

        val requestDto = ERequestDTO()

        requestDto.languageIds = Sets.newHashSet(1L)
        requestDto.datetime = LocalDateTime.now().plusDays(5L)
        requestDto.ocation = "ocation"
        requestDto.city = "Cologne"
        requestDto.street = "Street 123"
        requestDto.postalCode = "12345"
        requestDto.email = "request@email.de"
        requestDto.phone = "0221 123123"

        val response = restTemplate.postForEntity(buildUrl("placeRequest"), requestDto, PlaceRequestResult::class.java)

        assertTrue(response.statusCode.is2xxSuccessful)
        val data = response.body!!.requestId
        assertTrue(data.isNullOrBlank().not())

        assertEquals(1L, requestRepository.count())
        val requestIdentifier = requestRepository.findByRequestIdentifier_(data)
        assertTrue(requestIdentifier.isPresent)
        val request = requestIdentifier.get()

        assertTrue(request.languages?.filter { it.id == 1L }?.any() ?: false)
        assertEquals(ERequestStateEnum.OPEN, request.state)
        assertEquals("request@email.de", request.email)
    }

    @Test
    @DatabaseSetup(value = DBUnitData.BASE, type = DatabaseOperation.CLEAN_INSERT)
    fun testPlaceRequest2() {
        assertEquals(0L, requestRepository.count())

        val requestDto = ERequestDTO()

        requestDto.languageIds = Sets.newHashSet(1L, 2L, 3L)
        requestDto.datetime = LocalDateTime.now().plusDays(5L)
        requestDto.ocation = "ocation"
        requestDto.city = "Cologne"
        requestDto.street = "Street 123"
        requestDto.postalCode = "12345"
        requestDto.email = "request@email.de"
        requestDto.phone = "0221 123123"

        val response = restTemplate.postForEntity(buildUrl("placeRequest"), requestDto, PlaceRequestResult::class.java)

        assertTrue(response.statusCode.is2xxSuccessful)
        val data = response.body!!.requestId
        assertTrue(data.isNullOrBlank().not())

        assertEquals(1L, requestRepository.count())
        val requestIdentifier = requestRepository.findByRequestIdentifier_(data)
        assertTrue(requestIdentifier.isPresent)
        val request = requestIdentifier.get()

        assertTrue(request.languages?.filter { it.id == 1L }?.any() ?: false)
        assertEquals(ERequestStateEnum.OPEN, request.state)
        assertEquals("request@email.de", request.email)
    }

    @Test
    @DatabaseSetup(value = DBUnitData.BASE, type = DatabaseOperation.CLEAN_INSERT)
    fun testPlaceRequestFail() {
        val emptyRequest = ERequestDTO()

        try {
            restTemplate.postForEntity(buildUrl("placeRequest"), emptyRequest, String::class.java)
            fail("request should fail")
        } catch (e: RestClientException) {
            assertTrue(HttpClientErrorException::class.java.isInstance(e))
            val e2 = e as HttpClientErrorException
            assertEquals(HttpStatus.BAD_REQUEST, e2.statusCode)
        }
    }

    @Test
    @DatabaseSetups(
            DatabaseSetup(value = DBUnitData.BASE),
            DatabaseSetup(value = DBUnitData.REQUEST_OPEN, type = DatabaseOperation.REFRESH))
    fun testDeclineRequest() {
        assertTrue(requestRepository.findByRequestIdentifier("ident1").isPresent)
        assertEquals(ERequestStateEnum.OPEN,
                requestRepository.findByRequestIdentifier("ident1").get().state)
        val response = restTemplate.getForEntity(buildUrl("declineRequest/ident1/"), Any::class.java)

        assertTrue(response.statusCode.is2xxSuccessful)
        assertEquals(ERequestStateEnum.CANCELED,
                requestRepository.findByRequestIdentifier("ident1").get().state)
    }

    @Test
    @DatabaseSetups(
            DatabaseSetup(value = DBUnitData.BASE),
            DatabaseSetup(value = DBUnitData.REQUEST_OPEN, type = DatabaseOperation.REFRESH))
    fun testAnswerVoucherYes() {
        assertTrue(voucherRepository.findByIdentifier("voucherId1").isPresent)
        assertEquals(null, voucherRepository.findByIdentifier("voucherId1").get().answer)
        val response = restTemplate.postForEntity(buildUrl("answerRequest/voucherId1/yes"), null, Any::class.java)

        assertTrue(response.statusCode.is2xxSuccessful)
        assertEquals(true, voucherRepository.findByIdentifier("voucherId1").get().answer)
    }

    @Test
    @DatabaseSetups(
            DatabaseSetup(value = DBUnitData.BASE),
            DatabaseSetup(value = DBUnitData.REQUEST_OPEN, type = DatabaseOperation.REFRESH))
    fun testAnswerVoucherStatusIsOk() {
        assertTrue(voucherRepository.findByIdentifier("voucherId1").isPresent)
        assertEquals(null, voucherRepository.findByIdentifier("voucherId1").get().answer)
        val response = restTemplate.getForEntity<Any>(buildUrl("answerRequest/voucherId1/status"))

        assertTrue(response.statusCode.is2xxSuccessful)
        assertEquals(null, voucherRepository.findByIdentifier("voucherId1").get().answer)
    }

    @Test
    @DatabaseSetups(
            DatabaseSetup(value = DBUnitData.BASE),
            DatabaseSetup(value = DBUnitData.REQUEST_CANCELED, type = DatabaseOperation.REFRESH))
    @Ignore("doesnt work yet") //TODO:
    fun testAnswerVoucherStatusIsCanceled() {
        assertTrue(voucherRepository.findByIdentifier("voucherId1").isPresent)
        assertEquals(null, voucherRepository.findByIdentifier("voucherId1").get().answer)
        val response = restTemplate.getForEntity<Any>(buildUrl("answerRequest/voucherId1/status"), null!!, Any::class.java)

        assertTrue(response.statusCode.is2xxSuccessful)
        assertEquals(null, voucherRepository.findByIdentifier("voucherId1").get().answer)

        val body = response.body as Map<String, String>

        assertEquals(RequestWasCanceledException.MSG, body["msg"])
    }

    @Test
    @DatabaseSetups(
            DatabaseSetup(value = DBUnitData.BASE),
            DatabaseSetup(value = DBUnitData.REQUEST_OPEN, type = DatabaseOperation.REFRESH))
    fun testAnswerVoucherNo() {
        assertTrue(voucherRepository.findByIdentifier("voucherId1").isPresent)
        assertEquals(null, voucherRepository.findByIdentifier("voucherId1").get().answer)
        val response = restTemplate.postForEntity(buildUrl("answerRequest/voucherId1/no"), null, Any::class.java)

        assertTrue(response.statusCode.is2xxSuccessful)
        assertEquals(false, voucherRepository.findByIdentifier("voucherId1").get().answer)
    }

    @Test
    @DatabaseSetups(
            DatabaseSetup(value = DBUnitData.BASE),
            DatabaseSetup(value = DBUnitData.REQUEST_CANCELED, type = DatabaseOperation.REFRESH))
    fun testCanceledRequestAnswerVoucherYesFails() {
        assertTrue(voucherRepository.findByIdentifier("voucherId1").isPresent)
        assertEquals(null, voucherRepository.findByIdentifier("voucherId1").get().answer)
        val response = restTemplate.postForEntity(buildUrl("answerRequest/voucherId1/yes"), null, Any::class.java)

        assertTrue(response.statusCode.is2xxSuccessful)
        assertEquals(null, voucherRepository.findByIdentifier("voucherId1").get().answer)
    }

    @Test
    @DatabaseSetups(
            DatabaseSetup(value = DBUnitData.BASE),
            DatabaseSetup(value = DBUnitData.REQUEST_CANCELED, type = DatabaseOperation.REFRESH))
            /**
             * if the request was canceled, the volunteer can still answer no. check
             * requirements
             */
    fun testCanceledRequestAnswerVoucherNo() {
        assertTrue(voucherRepository.findByIdentifier("voucherId1").isPresent)
        assertEquals(null, voucherRepository.findByIdentifier("voucherId1").get().answer)
        val response = restTemplate.postForEntity(buildUrl("answerRequest/voucherId1/no"), null, Any::class.java)

        assertTrue(response.statusCode.is2xxSuccessful)
        assertEquals(false, voucherRepository.findByIdentifier("voucherId1").get().answer)
    }
}
