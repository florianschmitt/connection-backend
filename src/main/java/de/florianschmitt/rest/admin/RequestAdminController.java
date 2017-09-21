package de.florianschmitt.rest.admin;

import de.florianschmitt.model.entities.EVoucher;
import de.florianschmitt.model.rest.EVoucherDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.florianschmitt.model.entities.ERequest;
import de.florianschmitt.model.rest.ERequestDTO;
import de.florianschmitt.service.RequestService;
import de.florianschmitt.service.util.DTOMapper;

import java.util.Collection;

@RestController
@RequestMapping("/admin/request")
class RequestAdminController {

    @Autowired
    private RequestService service;

    @Autowired
    private DTOMapper mapper;

    @RequestMapping(path = "/get/{requestIdentifier}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<ERequestDTO> get(@PathVariable(name = "requestIdentifier") String requestIdentifier) {
        ERequest request = service.findByRequestIdentifier(requestIdentifier);
        ERequestDTO result = mapper.map(request, ERequestDTO.class);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(path = "/getAnswers/{requestIdentifier}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<Collection<EVoucherDTO>> getAnswers(@PathVariable(name = "requestIdentifier") String requestIdentifier) {
        Collection<EVoucher> vouchers = service.listVouchers(requestIdentifier);
        Collection<EVoucherDTO> result = mapper.map(vouchers, EVoucherDTO.class);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @RequestMapping(path = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<Page<ERequestDTO>> all() {
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
        Page<ERequest> page = service.all(pageable);
        Page<ERequestDTO> result = mapper.map(page, ERequestDTO.class);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(path = "/notPayed", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<Page<ERequestDTO>> notPayed() {
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
        Page<ERequest> page = service.findAllNotPayed(pageable);
        Page<ERequestDTO> result = mapper.map(page, ERequestDTO.class);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
