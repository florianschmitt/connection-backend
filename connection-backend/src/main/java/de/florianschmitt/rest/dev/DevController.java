package de.florianschmitt.rest.dev;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Sets;

import de.florianschmitt.model.entities.ELanguage;
import de.florianschmitt.model.entities.ERequest;
import de.florianschmitt.service.LanguageService;
import de.florianschmitt.service.RequestService;
import de.florianschmitt.system.util.DevProfile;

@RestController
@RequestMapping("/dev/")
@DevProfile
class DevController {

    @Autowired
    private RequestService requestService;

    @Autowired
    private LanguageService languageService;

    @RequestMapping(path = "/createRequests", method = RequestMethod.GET)
    ResponseEntity<?> createRequests(@RequestParam(defaultValue = "10", name = "count") int count) {
        Page<ELanguage> findAll = languageService.findAll(new PageRequest(0, 10));
        int numberOfElements = findAll.getNumberOfElements();

        IntStream.range(0, count).forEach(i -> {
            int randomInt = ThreadLocalRandom.current().nextInt(numberOfElements);
            ELanguage language = findAll.getContent().get(randomInt);
            ERequest r = new ERequest();
            r.setLanguages(Sets.newHashSet(language));
            r.setDatetime(LocalDateTime.now().plusDays(ThreadLocalRandom.current().nextInt(50)));
            r.setOcation("Anlass");
            r.setCity("Koeln");
            r.setPostalCode("12345");
            r.setStreet("Straßenname");
            r.setEmail("email@adresse.de");
            r.setPhone("0123456");
            requestService.submitNewRequest(r);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
