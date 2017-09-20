package de.florianschmitt.rest

import com.github.springtestdbunit.annotation.DatabaseOperation
import com.github.springtestdbunit.annotation.DatabaseSetup
import com.github.springtestdbunit.annotation.DatabaseSetups
import de.florianschmitt.model.rest.EFeedbackDTO
import de.florianschmitt.repository.FeedbackRepository
import de.florianschmitt.repository.RequestRepository
import de.florianschmitt.repository.VolunteerRepository
import de.florianschmitt.rest.base.BaseRestTest
import de.florianschmitt.rest.base.DBUnitData
import de.florianschmitt.rest.exception.RequestNotYetFinishedException
import org.junit.Assert
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FeedbackControllerTest : BaseRestTest() {

    @Autowired
    private lateinit var feedbackRepository: FeedbackRepository

    @Autowired
    private lateinit var volunteerRepository: VolunteerRepository

    @Autowired
    private lateinit var requestRepository: RequestRepository

    @Test
    @DatabaseSetups(
            DatabaseSetup(value = DBUnitData.BASE),
            DatabaseSetup(value = DBUnitData.REQUEST_FINISHED, type = DatabaseOperation.REFRESH))
    fun testFeedbackPositive() {
        val volunteer = volunteerRepository.findOne(1L)
        val request = requestRepository.findOne(1L)
        var feedback = feedbackRepository.findByVolunteerAndRequest(volunteer, request)

        assertFalse(feedback.isPresent)

        val dto = EFeedbackDTO(true, null)
        val response = restTemplate.postForEntity(buildUrl("feedback/ident3"), dto, Any::class.java)

        assertEquals(HttpStatus.OK, response.statusCode)

        feedback = feedbackRepository.findByVolunteerAndRequest(volunteer, request)

        assertTrue(feedback.isPresent)
        assertTrue(feedback.get().positive)
    }

    @Test
    @DatabaseSetups(
            DatabaseSetup(value = DBUnitData.BASE),
            DatabaseSetup(value = DBUnitData.REQUEST_FINISHED, type = DatabaseOperation.REFRESH))
    fun testFeedbackNegative() {
        val volunteer = volunteerRepository.findOne(1L)
        val request = requestRepository.findOne(1L)
        var feedback = feedbackRepository.findByVolunteerAndRequest(volunteer, request)
        Assert.assertFalse(feedback.isPresent)
        val comment = "Kommentar"
        val dto = EFeedbackDTO(false, comment)
        val response = restTemplate.postForEntity(buildUrl("feedback/ident3"), dto, Any::class.java)
        Assert.assertEquals(HttpStatus.OK, response.statusCode)
        feedback = feedbackRepository.findByVolunteerAndRequest(volunteer, request)
        Assert.assertTrue(feedback.isPresent)
        Assert.assertFalse(feedback.get().positive)
        Assert.assertEquals(comment, feedback.get().comment)
    }

    @Test
    @DatabaseSetups(
            DatabaseSetup(value = DBUnitData.BASE),
            DatabaseSetup(value = DBUnitData.REQUEST_OPEN, type = DatabaseOperation.REFRESH))
    fun testFeedbackNotFinishedFails() {
        val volunteer = volunteerRepository.findOne(1L)
        val request = requestRepository.findOne(1L)
        val feedback = feedbackRepository.findByVolunteerAndRequest(volunteer, request)
        Assert.assertFalse(feedback.isPresent)
        val dto = EFeedbackDTO(true, null)

        val response = restTemplate.postForEntity(buildUrl("feedback/ident1"), dto, Any::class.java)
        Assert.assertEquals(HttpStatus.OK, response.statusCode)

        val body = response.body as Map<String, String>

        Assert.assertEquals(RequestNotYetFinishedException::class.java.name, body["exception"])
        Assert.assertEquals(RequestNotYetFinishedException.MSG, body["message"])
    }
}
