package de.florianschmitt.rest;

import java.util.Map;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.google.common.collect.Sets;

import de.florianschmitt.model.entities.EVolunteer;
import de.florianschmitt.model.rest.EVolunteerDTO;
import de.florianschmitt.repository.LanguageRepository;
import de.florianschmitt.repository.VolunteerRepository;
import de.florianschmitt.rest.base.BaseRestTest;
import de.florianschmitt.rest.base.DBUnitData;

@SuppressWarnings("unchecked")
@DatabaseSetup(value = DBUnitData.BASE, type = DatabaseOperation.CLEAN_INSERT)
@DatabaseSetup(value = DBUnitData.ADMIN_USER, type = DatabaseOperation.REFRESH)
public class VolunteerControllerTest extends BaseRestTest {

    @Autowired
    private VolunteerRepository volunteerRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Test
    public void testAll() {
        @SuppressWarnings("rawtypes")
        ResponseEntity<Map> response = restTemplate.getForEntity(buildUrl("admin/volunteer/all"), Map.class);
        Map<String, Object> body = response.getBody();
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals(2, body.get("totalElements"));
    }

    @Test
    public void testGetOne() {
        ResponseEntity<EVolunteerDTO> response = restTemplate.getForEntity(buildUrl("admin/volunteer/get/1"),
                EVolunteerDTO.class);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        EVolunteerDTO result = response.getBody();
        Assert.assertEquals("firstname", result.getFirstname());
        Assert.assertEquals("lastname", result.getLastname());
        Assert.assertEquals(true, result.isActive());
        Assert.assertThat(result.getLanguageIds(), Matchers.hasItems(1L, 2L));
    }

    @Test
    public void testSaveNew() {
        EVolunteerDTO dto = new EVolunteerDTO();
        dto.setFirstname("newfirstname");
        dto.setLastname("newlastname");
        dto.setEmail("new@mail.de");
        dto.setActive(true);
        dto.setLanguageIds(Sets.newHashSet(1L));

        ResponseEntity<?> response = restTemplate.postForEntity(buildUrl("admin/volunteer/save"), dto, Object.class);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());

        Optional<EVolunteer> newVolunteerO = volunteerRepository.findOneWithLanguages(3L);
        Assert.assertTrue(newVolunteerO.isPresent());
        EVolunteer newVolunteer = newVolunteerO.get();

        Assert.assertEquals("newfirstname", newVolunteer.getFirstname());
        Assert.assertEquals("newlastname", newVolunteer.getLastname());
        Assert.assertEquals("new@mail.de", newVolunteer.getEmail());
        Assert.assertEquals(true, newVolunteer.isActive());
        Assert.assertThat(newVolunteer.getLanguages(), Matchers.hasItems(languageRepository.findOne(1L)));
    }

    @Test
    public void testSaveExisting() {
        EVolunteerDTO dto = new EVolunteerDTO();
        dto.setId(1L);
        dto.setFirstname("newfirstname");
        dto.setLastname("newlastname");
        dto.setEmail("new@mail.de");
        dto.setActive(true);
        dto.setLanguageIds(Sets.newHashSet(1L));

        ResponseEntity<?> response = restTemplate.postForEntity(buildUrl("admin/volunteer/save"), dto, Object.class);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());

        Optional<EVolunteer> newVolunteerO = volunteerRepository.findOneWithLanguages(1L);
        Assert.assertTrue(newVolunteerO.isPresent());
        EVolunteer newVolunteer = newVolunteerO.get();

        Assert.assertEquals("newfirstname", newVolunteer.getFirstname());
        Assert.assertEquals("newlastname", newVolunteer.getLastname());
        Assert.assertEquals("new@mail.de", newVolunteer.getEmail());
        Assert.assertEquals(true, newVolunteer.isActive());
        Assert.assertThat(newVolunteer.getLanguages(), Matchers.hasItems(languageRepository.findOne(1L)));
    }

    @Override
    protected RestTemplateBuilder customize(RestTemplateBuilder restBuilder) {
        return restBuilder.basicAuthorization("admin@connection.de", "admin123");
    }
}
