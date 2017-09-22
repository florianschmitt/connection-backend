package de.florianschmitt.rest

import com.github.springtestdbunit.annotation.DatabaseOperation
import com.github.springtestdbunit.annotation.DatabaseSetup
import com.github.springtestdbunit.annotation.DatabaseSetups
import com.google.common.collect.Sets
import de.florianschmitt.base.BaseRestTest
import de.florianschmitt.base.DBUnitData
import de.florianschmitt.model.rest.EVolunteerDTO
import de.florianschmitt.repository.LanguageRepository
import de.florianschmitt.repository.VolunteerRepository
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpStatus
import org.springframework.web.client.getForEntity
import org.springframework.web.client.postForEntity
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@DatabaseSetups(
        DatabaseSetup(value = DBUnitData.BASE),
        DatabaseSetup(value = DBUnitData.ADMIN_USER, type = DatabaseOperation.REFRESH))
class VolunteerControllerTest : BaseRestTest() {

    @Autowired
    private lateinit var volunteerRepository: VolunteerRepository

    @Autowired
    private lateinit var languageRepository: LanguageRepository

    @Test
    fun testAll() {
        val response = restTemplate.getForEntity<Map<Any, Any>>(buildUrl("admin/volunteer/all"))
        val body = response.body
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(2, body["totalElements"])
    }

    @Test
    fun testGetOne() {
        val response = restTemplate.getForEntity<EVolunteerDTO>(buildUrl("admin/volunteer/get/1"))
        assertEquals(HttpStatus.OK, response.statusCode)
        val result = response.body
        assertEquals("firstname", result!!.firstname)
        assertEquals("lastname", result.lastname)
        assertEquals(true, result.isActive)
        assertTrue(result.languageIds?.containsAll(setOf(1L, 2L)) ?: false)
    }

    @Test
    fun testSaveNew() {
        val dto = EVolunteerDTO(null, "newfirstname", "newlastname",
                true, "new@mail.de", Sets.newHashSet(1L))

        val response = restTemplate.postForEntity<Any>(buildUrl("admin/volunteer/save"), dto)
        assertEquals(HttpStatus.OK, response.statusCode)


        val newVolunteerO = volunteerRepository!!.findByEmail("new@mail.de")
        assertTrue(newVolunteerO.isPresent)

        val newVolunteer = volunteerRepository.findOneWithLanguages(newVolunteerO.get().id!!).get()

        assertEquals("newfirstname", newVolunteer.firstname)
        assertEquals("newlastname", newVolunteer.lastname)
        assertEquals("new@mail.de", newVolunteer.email)
        assertEquals(true, newVolunteer.isActive)
        assertTrue(newVolunteer.languages?.contains(languageRepository.findById(1L).get()) ?: false)
    }

    @Test
    fun testSaveExisting() {
        val dto = EVolunteerDTO(1L, "newfirstname", "newlastname",
                true, "new@mail.de", Sets.newHashSet(1L))

        val response = restTemplate.postForEntity<Any>(buildUrl("admin/volunteer/save"), dto)
        assertEquals(HttpStatus.OK, response.statusCode)

        val newVolunteerO = volunteerRepository!!.findOneWithLanguages(1L)
        assertTrue(newVolunteerO.isPresent)
        val newVolunteer = newVolunteerO.get()

        assertEquals("newfirstname", newVolunteer.firstname)
        assertEquals("newlastname", newVolunteer.lastname)
        assertEquals("new@mail.de", newVolunteer.email)
        assertEquals(true, newVolunteer.isActive)
        assertTrue(newVolunteer.languages?.contains(languageRepository.findById(1L).get()) ?: false)
    }

    override fun customize(restBuilder: RestTemplateBuilder): RestTemplateBuilder {
        return restBuilder.basicAuthorization("admin@connection.de", "admin123")
    }
}
