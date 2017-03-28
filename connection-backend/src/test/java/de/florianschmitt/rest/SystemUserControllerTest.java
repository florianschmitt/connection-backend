package de.florianschmitt.rest;

import java.util.Map;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.HttpClientErrorException;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;

import de.florianschmitt.model.entities.ESystemUser;
import de.florianschmitt.model.rest.ESystemUserDTO;
import de.florianschmitt.repository.SystemUserRepository;
import de.florianschmitt.rest.base.BaseRestTest;
import de.florianschmitt.rest.base.DBUnitData;
import de.florianschmitt.rest.exception.SystemMustHaveAtLeastASingleActiveAdmin;

@SuppressWarnings("unchecked")
@DatabaseSetup(value = DBUnitData.BASE, type = DatabaseOperation.CLEAN_INSERT)
@DatabaseSetup(value = DBUnitData.ADMIN_USER, type = DatabaseOperation.REFRESH)
public class SystemUserControllerTest extends BaseRestTest {

    @Autowired
    private SystemUserRepository systemUserRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Test
    public void testAll() {
        @SuppressWarnings("rawtypes")
        ResponseEntity<Map> response = restTemplate.getForEntity(buildUrl("admin/systemuser/all"), Map.class);
        Map<String, Object> body = response.getBody();
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals(1, body.get("totalElements"));
    }

    @Test
    public void testGetOne() {
        ResponseEntity<ESystemUserDTO> response = restTemplate.getForEntity(buildUrl("admin/systemuser/get/1"),
                ESystemUserDTO.class);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        ESystemUserDTO result = response.getBody();
        Assert.assertEquals("admin", result.getFirstname());
        Assert.assertEquals("user", result.getLastname());
        Assert.assertEquals(true, result.getActive());
    }

    @Test
    public void testSaveNew() {
        ESystemUserDTO dto = new ESystemUserDTO();
        dto.setFirstname("newfirstname");
        dto.setLastname("newlastname");
        dto.setEmail("new@mail.de");
        dto.setCleartextPassword("password");
        dto.setActive(true);

        ResponseEntity<?> response = restTemplate.postForEntity(buildUrl("admin/systemuser/save"), dto, Object.class);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());

        ESystemUser newVolunteer = systemUserRepository.findOne(2L);

        Assert.assertEquals("newfirstname", newVolunteer.getFirstname());
        Assert.assertEquals("newlastname", newVolunteer.getLastname());
        Assert.assertEquals("new@mail.de", newVolunteer.getEmail());
        Assert.assertEquals(true, newVolunteer.isActive());
        Assert.assertTrue(encoder.matches("password", newVolunteer.getHashedPassword()));
    }

    @Test
    public void testSaveExisting() {
        ESystemUserDTO dto = new ESystemUserDTO();
        dto.setId(1L);
        dto.setFirstname("newfirstname");
        dto.setLastname("newlastname");
        dto.setEmail("admin@connection.de");
        dto.setActive(true);
        dto.setHasAdminRight(true);

        ResponseEntity<?> response = restTemplate.postForEntity(buildUrl("admin/systemuser/save"), dto, Object.class);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());

        ESystemUser saved = systemUserRepository.findOne(1L);

        Assert.assertEquals("newfirstname", saved.getFirstname());
        Assert.assertEquals("newlastname", saved.getLastname());
        Assert.assertEquals("admin@connection.de", saved.getEmail());
        Assert.assertEquals(true, saved.isActive());
        Assert.assertEquals(true, saved.getHasAdminRight());
        Assert.assertTrue(encoder.matches("admin123", saved.getHashedPassword()));
    }

    @Test
    @Ignore
    public void testSaveExisting2() {
        ESystemUserDTO dto = new ESystemUserDTO();
        dto.setId(1L);
        dto.setFirstname("newfirstname");
        dto.setLastname("newlastname");
        dto.setEmail("admin@connection.de");
        dto.setActive(false);
        dto.setHasAdminRight(true);

        ResponseEntity<?> response = restTemplate.postForEntity(buildUrl("admin/systemuser/save"), dto, Object.class);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ESystemUser saved = systemUserRepository.findOne(1L);

        Assert.assertEquals("newfirstname", saved.getFirstname());
        Assert.assertEquals("newlastname", saved.getLastname());
        Assert.assertEquals("admin@connection.de", saved.getEmail());
        Assert.assertEquals(false, saved.isActive());
        Assert.assertTrue(encoder.matches("admin123", saved.getHashedPassword()));
    }

    @Test
    public void testSaveExistingChangePassword() {
        ESystemUserDTO dto = new ESystemUserDTO();
        dto.setId(1L);
        dto.setFirstname("newfirstname");
        dto.setLastname("newlastname");
        dto.setEmail("admin@connection.de");
        dto.setCleartextPassword("neuesPasswort123");
        dto.setActive(true);
        dto.setHasAdminRight(true);

        ResponseEntity<?> response = restTemplate.postForEntity(buildUrl("admin/systemuser/save"), dto, Object.class);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());

        ESystemUser saved = systemUserRepository.findOne(1L);

        Assert.assertEquals("newfirstname", saved.getFirstname());
        Assert.assertEquals("newlastname", saved.getLastname());
        Assert.assertEquals("admin@connection.de", saved.getEmail());
        Assert.assertEquals(true, saved.isActive());
        Assert.assertEquals(true, saved.getHasAdminRight());
        Assert.assertTrue(encoder.matches("neuesPasswort123", saved.getHashedPassword()));
    }

    @Test
    public void testSaveExistingSetInactiveFails() {
        ESystemUserDTO dto = new ESystemUserDTO();
        dto.setId(1L);
        dto.setFirstname("newfirstname");
        dto.setLastname("newlastname");
        dto.setEmail("admin@connection.de");
        dto.setActive(false);
        dto.setHasAdminRight(true);
        try {
            restTemplate.postForEntity(buildUrl("admin/systemuser/save"), dto, Object.class);
            Assert.fail("save should fail");
        } catch (HttpClientErrorException e) {
            Assert.assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());

            Map<String, String> body = fromJsonStringToMap(e.getResponseBodyAsString());
            Assert.assertEquals(SystemMustHaveAtLeastASingleActiveAdmin.class.getName(), body.get("exception"));
            Assert.assertEquals(SystemMustHaveAtLeastASingleActiveAdmin.MSG, body.get("message"));
        }

        ESystemUser saved = systemUserRepository.findOne(1L);

        Assert.assertEquals("admin", saved.getFirstname());
        Assert.assertEquals("user", saved.getLastname());
        Assert.assertEquals("admin@connection.de", saved.getEmail());
        Assert.assertEquals(true, saved.isActive());
        Assert.assertEquals(true, saved.getHasAdminRight());
    }

    @Override
    protected RestTemplateBuilder customize(RestTemplateBuilder restBuilder) {
        return restBuilder.basicAuthorization("admin@connection.de", "admin123");
    }
}
