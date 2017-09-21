package de.florianschmitt.rest;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.google.common.collect.Sets;
import de.florianschmitt.model.entities.ERequest;
import de.florianschmitt.model.entities.ERequestStateEnum;
import de.florianschmitt.model.rest.ERequestDTO;
import de.florianschmitt.repository.RequestRepository;
import de.florianschmitt.repository.VoucherRepository;
import de.florianschmitt.rest.base.BaseRestTest;
import de.florianschmitt.rest.base.DBUnitData;
import de.florianschmitt.rest.exception.RequestWasCanceledException;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

public class RequestControllerTest extends BaseRestTest {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private VoucherRepository voucherRepository;

    @Test
    @DatabaseSetup(value = DBUnitData.BASE, type = DatabaseOperation.CLEAN_INSERT)
    public void testPlaceRequest() {
        Assert.assertEquals(0L, requestRepository.count());

        ERequestDTO requestDto = new ERequestDTO();

        requestDto.setLanguageIds(Sets.newHashSet(1L));
        requestDto.setDatetime(LocalDateTime.now().plusDays(5L));
        requestDto.setOcation("ocation");
        requestDto.setCity("Cologne");
        requestDto.setStreet("Street 123");
        requestDto.setPostalCode("12345");
        requestDto.setEmail("request@email.de");
        requestDto.setPhone("0221 123123");

        ResponseEntity<PlaceRequestResult> response = restTemplate.postForEntity(buildUrl("placeRequest"), requestDto, PlaceRequestResult.class);

        Assert.assertTrue(response.getStatusCode().is2xxSuccessful());
        String data = response.getBody().getRequestId();
        Assert.assertThat(data, Matchers.not(Matchers.isEmptyOrNullString()));

        Assert.assertEquals(1L, requestRepository.count());
        Optional<ERequest> requestIdentifier = requestRepository.findByRequestIdentifier_(data);
        Assert.assertTrue(requestIdentifier.isPresent());
        ERequest request = requestIdentifier.get();

        Assert.assertThat(request.getLanguages(), Matchers.hasItem((Matchers.hasProperty("id", Matchers.equalTo(1L)))));
        Assert.assertEquals(ERequestStateEnum.OPEN, request.getState());
        Assert.assertEquals("request@email.de", request.getEmail());
    }

    @Test
    @DatabaseSetup(value = DBUnitData.BASE, type = DatabaseOperation.CLEAN_INSERT)
    public void testPlaceRequest2() {
        Assert.assertEquals(0L, requestRepository.count());

        ERequestDTO requestDto = new ERequestDTO();

        requestDto.setLanguageIds(Sets.newHashSet(1L, 2L, 3L));
        requestDto.setDatetime(LocalDateTime.now().plusDays(5L));
        requestDto.setOcation("ocation");
        requestDto.setCity("Cologne");
        requestDto.setStreet("Street 123");
        requestDto.setPostalCode("12345");
        requestDto.setEmail("request@email.de");
        requestDto.setPhone("0221 123123");

        ResponseEntity<PlaceRequestResult> response = restTemplate.postForEntity(buildUrl("placeRequest"), requestDto, PlaceRequestResult.class);

        Assert.assertTrue(response.getStatusCode().is2xxSuccessful());
        String data = response.getBody().getRequestId();
        Assert.assertThat(data, Matchers.not(Matchers.isEmptyOrNullString()));

        Assert.assertEquals(1L, requestRepository.count());
        Optional<ERequest> requestIdentifier = requestRepository.findByRequestIdentifier_(data);
        Assert.assertTrue(requestIdentifier.isPresent());
        ERequest request = requestIdentifier.get();

        Assert.assertThat(request.getLanguages(), Matchers.hasItem((Matchers.hasProperty("id", Matchers.equalTo(1L)))));
        Assert.assertEquals(ERequestStateEnum.OPEN, request.getState());
        Assert.assertEquals("request@email.de", request.getEmail());
    }

    @Test
    @DatabaseSetup(value = DBUnitData.BASE, type = DatabaseOperation.CLEAN_INSERT)
    public void testPlaceRequestFail() {
        ERequestDTO emptyRequest = new ERequestDTO();

        try {
            restTemplate.postForEntity(buildUrl("placeRequest"), emptyRequest, String.class);
            Assert.fail("request should fail");
        } catch (RestClientException e) {
            Assert.assertTrue(HttpClientErrorException.class.isInstance(e));
            HttpClientErrorException e2 = (HttpClientErrorException) e;
            Assert.assertEquals(HttpStatus.BAD_REQUEST, e2.getStatusCode());
        }
    }

    @Test
    @DatabaseSetup(value = DBUnitData.BASE, type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseSetup(value = DBUnitData.REQUEST_OPEN, type = DatabaseOperation.REFRESH)
    public void testDeclineRequest() {
        Assert.assertTrue(requestRepository.findByRequestIdentifier("ident1").isPresent());
        Assert.assertEquals(ERequestStateEnum.OPEN,
                requestRepository.findByRequestIdentifier("ident1").get().getState());
        ResponseEntity<?> response = restTemplate.getForEntity(buildUrl("declineRequest/ident1/"), Object.class);

        Assert.assertTrue(response.getStatusCode().is2xxSuccessful());
        Assert.assertEquals(ERequestStateEnum.CANCELED,
                requestRepository.findByRequestIdentifier("ident1").get().getState());
    }

    @Test
    @DatabaseSetup(value = DBUnitData.BASE, type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseSetup(value = DBUnitData.REQUEST_OPEN, type = DatabaseOperation.REFRESH)
    public void testAnswerVoucherYes() {
        Assert.assertTrue(voucherRepository.findByIdentifier("voucherId1").isPresent());
        Assert.assertEquals(null, voucherRepository.findByIdentifier("voucherId1").get().getAnswer());
        ResponseEntity<?> response = restTemplate.postForEntity(buildUrl("answerRequest/voucherId1/yes"), null, Object.class);

        Assert.assertTrue(response.getStatusCode().is2xxSuccessful());
        Assert.assertEquals(true, voucherRepository.findByIdentifier("voucherId1").get().getAnswer());
    }

    @Test
    @DatabaseSetup(value = DBUnitData.BASE, type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseSetup(value = DBUnitData.REQUEST_OPEN, type = DatabaseOperation.REFRESH)
    public void testAnswerVoucherStatusIsOk() {
        Assert.assertTrue(voucherRepository.findByIdentifier("voucherId1").isPresent());
        Assert.assertEquals(null, voucherRepository.findByIdentifier("voucherId1").get().getAnswer());
        ResponseEntity<?> response = restTemplate.getForEntity(buildUrl("answerRequest/voucherId1/status"), null, Object.class);

        Assert.assertTrue(response.getStatusCode().is2xxSuccessful());
        Assert.assertEquals(null, voucherRepository.findByIdentifier("voucherId1").get().getAnswer());
    }

    @Test
    @DatabaseSetup(value = DBUnitData.BASE, type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseSetup(value = DBUnitData.REQUEST_CANCELED, type = DatabaseOperation.REFRESH)
    @Ignore("doesnt work yet") //TODO:
    public void testAnswerVoucherStatusIsCanceled() {
        Assert.assertTrue(voucherRepository.findByIdentifier("voucherId1").isPresent());
        Assert.assertEquals(null, voucherRepository.findByIdentifier("voucherId1").get().getAnswer());
        ResponseEntity<?> response = restTemplate.getForEntity(buildUrl("answerRequest/voucherId1/status"), null, Object.class);

        Assert.assertTrue(response.getStatusCode().is2xxSuccessful());
        Assert.assertEquals(null, voucherRepository.findByIdentifier("voucherId1").get().getAnswer());

        @SuppressWarnings("unchecked")
        Map<String, String> body = (Map<String, String>) response.getBody();

        Assert.assertEquals(RequestWasCanceledException.MSG, body.get("msg"));
    }

    @Test
    @DatabaseSetup(value = DBUnitData.BASE, type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseSetup(value = DBUnitData.REQUEST_OPEN, type = DatabaseOperation.REFRESH)
    public void testAnswerVoucherNo() {
        Assert.assertTrue(voucherRepository.findByIdentifier("voucherId1").isPresent());
        Assert.assertEquals(null, voucherRepository.findByIdentifier("voucherId1").get().getAnswer());
        ResponseEntity<?> response = restTemplate.postForEntity(buildUrl("answerRequest/voucherId1/no"), null, Object.class);

        Assert.assertTrue(response.getStatusCode().is2xxSuccessful());
        Assert.assertEquals(false, voucherRepository.findByIdentifier("voucherId1").get().getAnswer());
    }

    @Test
    @DatabaseSetup(value = DBUnitData.BASE, type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseSetup(value = DBUnitData.REQUEST_CANCELED, type = DatabaseOperation.REFRESH)
    public void testCanceledRequestAnswerVoucherYesFails() {
        Assert.assertTrue(voucherRepository.findByIdentifier("voucherId1").isPresent());
        Assert.assertEquals(null, voucherRepository.findByIdentifier("voucherId1").get().getAnswer());
        ResponseEntity<?> response = restTemplate.postForEntity(buildUrl("answerRequest/voucherId1/yes"), null, Object.class);

        Assert.assertTrue(response.getStatusCode().is2xxSuccessful());

        @SuppressWarnings("unchecked")
        Map<String, String> body = (Map<String, String>) response.getBody();

        Assert.assertEquals(null, voucherRepository.findByIdentifier("voucherId1").get().getAnswer());
    }

    @Test
    @DatabaseSetup(value = DBUnitData.BASE, type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseSetup(value = DBUnitData.REQUEST_CANCELED, type = DatabaseOperation.REFRESH)
    /**
     * if the request was canceled, the volunteer can still answer no. check
     * requirements
     */
    public void testCanceledRequestAnswerVoucherNo() {
        Assert.assertTrue(voucherRepository.findByIdentifier("voucherId1").isPresent());
        Assert.assertEquals(null, voucherRepository.findByIdentifier("voucherId1").get().getAnswer());
        ResponseEntity<?> response = restTemplate.postForEntity(buildUrl("answerRequest/voucherId1/no"), null, Object.class);

        Assert.assertTrue(response.getStatusCode().is2xxSuccessful());
        Assert.assertEquals(false, voucherRepository.findByIdentifier("voucherId1").get().getAnswer());
    }
}
