package de.florianschmitt.rest

import com.github.springtestdbunit.annotation.DatabaseOperation
import com.github.springtestdbunit.annotation.DatabaseSetup
import com.github.springtestdbunit.annotation.DatabaseSetups
import de.florianschmitt.base.BaseRestTest
import de.florianschmitt.base.DBUnitData
import de.florianschmitt.model.rest.ESystemUserDTO
import de.florianschmitt.repository.SystemUserRepository
import de.florianschmitt.rest.exception.SystemMustHaveAtLeastASingleActiveAdmin
import org.junit.Ignore
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.getForEntity
import org.springframework.web.client.postForEntity
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

@DatabaseSetups(
        DatabaseSetup(value = DBUnitData.BASE),
        DatabaseSetup(value = DBUnitData.ADMIN_USER, type = DatabaseOperation.REFRESH))
class SystemUserControllerTest : BaseRestTest() {

    @Autowired
    private lateinit var systemUserRepository: SystemUserRepository

    @Autowired
    private lateinit var encoder: PasswordEncoder

    @Test
    fun testAll() {
        val response = restTemplate.getForEntity<Map<Any, Any>>(buildUrl("admin/systemuser/all"))
        val body = response.body
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(1, body!!["totalElements"])
    }

    @Test
    fun testGetOne() {
        val response = restTemplate.getForEntity<ESystemUserDTO>(buildUrl("admin/systemuser/get/1"))
        assertEquals(HttpStatus.OK, response.statusCode)
        val result = response.body
        assertEquals("admin", result!!.firstname)
        assertEquals("user", result.lastname)
        assertEquals(true, result.active)
    }

    @Test
    fun testSaveNew() {
        val dto = ESystemUserDTO()
        dto.firstname = "newfirstname"
        dto.lastname = "newlastname"
        dto.email = "new@mail.de"
        dto.cleartextPassword = "password"
        dto.active = true

        val response = restTemplate.postForEntity<Any>(buildUrl("admin/systemuser/save"), dto)
        assertEquals(HttpStatus.OK, response.statusCode)

        val newVolunteer = systemUserRepository.findByEmail("new@mail.de").get()

        assertEquals("newfirstname", newVolunteer.firstname)
        assertEquals("newlastname", newVolunteer.lastname)
        assertEquals("new@mail.de", newVolunteer.email)
        assertEquals(true, newVolunteer.isActive)
        assertTrue(encoder.matches("password", newVolunteer.hashedPassword))
    }

    @Test
    fun testSaveExisting() {
        val dto = ESystemUserDTO()
        dto.id = 1L
        dto.firstname = "newfirstname"
        dto.lastname = "newlastname"
        dto.email = "admin@connection.de"
        dto.active = true
        dto.hasAdminRight = true

        val response = restTemplate.postForEntity<Any>(buildUrl("admin/systemuser/save"), dto)
        assertEquals(HttpStatus.OK, response.statusCode)

        val saved = systemUserRepository.findById(1L).get()

        assertEquals("newfirstname", saved.firstname)
        assertEquals("newlastname", saved.lastname)
        assertEquals("admin@connection.de", saved.email)
        assertEquals(true, saved.isActive)
        assertEquals(true, saved.hasAdminRight)
        assertTrue(encoder.matches("admin123", saved.hashedPassword))
    }

    @Test
    @Ignore
    fun testSaveExisting2() {
        val dto = ESystemUserDTO()
        dto.id = 1L
        dto.firstname = "newfirstname"
        dto.lastname = "newlastname"
        dto.email = "admin@connection.de"
        dto.active = false
        dto.hasAdminRight = true

        val response = restTemplate.postForEntity<Any>(buildUrl("admin/systemuser/save"), dto)
        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)

        val saved = systemUserRepository.findById(1L).get()

        assertEquals("newfirstname", saved.firstname)
        assertEquals("newlastname", saved.lastname)
        assertEquals("admin@connection.de", saved.email)
        assertEquals(false, saved.isActive)
        assertTrue(encoder.matches("admin123", saved.hashedPassword))
    }

    @Test
    fun testSaveExistingChangePassword() {
        val dto = ESystemUserDTO()
        dto.id = 1L
        dto.firstname = "newfirstname"
        dto.lastname = "newlastname"
        dto.email = "admin@connection.de"
        dto.cleartextPassword = "neuesPasswort123"
        dto.active = true
        dto.hasAdminRight = true

        val response = restTemplate.postForEntity<Any>(buildUrl("admin/systemuser/save"), dto)
        assertEquals(HttpStatus.OK, response.statusCode)

        val saved = systemUserRepository.findById(1L).get()

        assertEquals("newfirstname", saved.firstname)
        assertEquals("newlastname", saved.lastname)
        assertEquals("admin@connection.de", saved.email)
        assertEquals(true, saved.isActive)
        assertEquals(true, saved.hasAdminRight)
        assertTrue(encoder.matches("neuesPasswort123", saved.hashedPassword))
    }

    @Test
    fun testSaveExistingSetInactiveFails() {
        val dto = ESystemUserDTO()
        dto.id = 1L
        dto.firstname = "newfirstname"
        dto.lastname = "newlastname"
        dto.email = "admin@connection.de"
        dto.active = false
        dto.hasAdminRight = true
        try {
            restTemplate.postForEntity<Any>(buildUrl("admin/systemuser/save"), dto)
            fail("save should fail")
        } catch (e: HttpClientErrorException) {
            assertEquals(HttpStatus.BAD_REQUEST, e.statusCode)

            val body = fromJsonStringToMap(e.responseBodyAsString)
            assertEquals(SystemMustHaveAtLeastASingleActiveAdmin.MSG, body["message"])
        }

        val saved = systemUserRepository.findById(1L).get()

        assertEquals("admin", saved.firstname)
        assertEquals("user", saved.lastname)
        assertEquals("admin@connection.de", saved.email)
        assertEquals(true, saved.isActive)
        assertEquals(true, saved.hasAdminRight)
    }

    override fun customize(restBuilder: RestTemplateBuilder): RestTemplateBuilder {
        return restBuilder.basicAuthorization("admin@connection.de", "admin123")
    }
}
