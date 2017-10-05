package de.florianschmitt.rest

import com.github.springtestdbunit.annotation.DatabaseOperation
import com.github.springtestdbunit.annotation.DatabaseSetup
import com.github.springtestdbunit.annotation.DatabaseSetups
import de.florianschmitt.base.BaseRestTest
import de.florianschmitt.base.DBUnitData
import de.florianschmitt.model.rest.ELanguageAdminDTO
import de.florianschmitt.model.rest.ELocalizedDTO
import de.florianschmitt.repository.LanguageRepository
import de.florianschmitt.repository.VolunteerRepository
import org.junit.Ignore
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
class LanguageAdminControllerTest : BaseRestTest() {

    @Autowired
    private lateinit var volunteerRepository: VolunteerRepository

    @Autowired
    private lateinit var languageRepository: LanguageRepository

    @Test
    fun testAll() {
        val response = restTemplate.getForEntity<Map<Any, Any>>(buildUrl("admin/language/all"))
        val body = response.body
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(3, body["totalElements"])
    }

    @Test
    fun testGetOne() {
        val response = restTemplate.getForEntity<ELanguageAdminDTO>(buildUrl("admin/language/get/1"))
        assertEquals(HttpStatus.OK, response.statusCode)
        val result = response.body
        assertEquals("ARABIC", result!!.identifier)
        assertEquals("Arabisch", result.localized?.filter { it.locale == "de" }?.single()?.value)
        assertEquals("العربية", result.localized?.filter { it.locale == "ar" }?.single()?.value)
        assertEquals("Arabic", result.localized?.filter { it.locale == "en" }?.single()?.value)
    }

    @Test
    @Ignore("TODO: does not work")
    fun testSaveNew() {
        val dto = ELanguageAdminDTO()
        dto.identifier = "SPANISH"
        dto.viewOrder = 10

        val l1 = ELocalizedDTO()
        l1.value = "Spanisch"
        l1.locale = "de"

        val l2 = ELocalizedDTO()
        l2.value = "Spanish"
        l2.locale = "en"

        dto.localized = listOf(l1, l2)

        val saveResponse = restTemplate.postForEntity<Any>(buildUrl("admin/language/save"), dto)
        assertEquals(HttpStatus.OK, saveResponse.statusCode)

        val response = restTemplate.getForEntity<ELanguageAdminDTO>(buildUrl("admin/language/get/4"))
        assertEquals(HttpStatus.OK, response.statusCode)
        val result = response.body
        assertEquals("SPANISH", result!!.identifier)
        assertEquals("Spanisch", result.localized?.filter { it.locale == "de" }?.single()?.value)
        assertEquals("Spanish", result.localized?.filter { it.locale == "en" }?.single()?.value)
    }

//    @Test
//    fun testSaveExisting() {
//        val dto = EVolunteerDTO(1L, "newfirstname", "newlastname",
//                true, "new@mail.de", Sets.newHashSet(1L))
//
//        val response = restTemplate.postForEntity<Any>(buildUrl("admin/volunteer/save"), dto)
//        assertEquals(HttpStatus.OK, response.statusCode)
//
//        val newVolunteerO = volunteerRepository!!.findOneWithLanguages(1L)
//        assertTrue(newVolunteerO.isPresent)
//        val newVolunteer = newVolunteerO.get()
//
//        assertEquals("newfirstname", newVolunteer.firstname)
//        assertEquals("newlastname", newVolunteer.lastname)
//        assertEquals("new@mail.de", newVolunteer.email)
//        assertEquals(true, newVolunteer.isActive)
//        assertTrue(newVolunteer.languages?.contains(languageRepository.findById(1L).get()) ?: false)
//    }

    override fun customize(restBuilder: RestTemplateBuilder): RestTemplateBuilder {
        return restBuilder.basicAuthorization("admin@connection.de", "admin123")
    }
}