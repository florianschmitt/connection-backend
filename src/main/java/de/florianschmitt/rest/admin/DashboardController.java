package de.florianschmitt.rest.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.florianschmitt.model.rest.EDashboardDTO;
import de.florianschmitt.service.DashboardService;

@RestController
@RequestMapping("/admin/dashboard")
class DashboardController {

    @Autowired
    private DashboardService service;

    @RequestMapping(path = "/info", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<EDashboardDTO> get() {
        EDashboardDTO info = service.info();
        return new ResponseEntity<>(info, HttpStatus.OK);
    }
}
