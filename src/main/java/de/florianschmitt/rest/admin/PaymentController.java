package de.florianschmitt.rest.admin;

import de.florianschmitt.model.entities.EPayment;
import de.florianschmitt.model.entities.ERequest;
import de.florianschmitt.model.entities.ESystemUser;
import de.florianschmitt.model.rest.EPaymentDTO;
import de.florianschmitt.service.PaymentService;
import de.florianschmitt.service.RequestService;
import de.florianschmitt.service.util.DTOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/payment")
class PaymentController {

    @Autowired
    private PaymentService service;

    @Autowired
    private RequestService requestService;

    @Autowired
    private DTOMapper mapper;

    @RequestMapping(path = "/placePayment", method = RequestMethod.POST)
    public ResponseEntity<?> placePayment(@RequestBody @Valid EPaymentDTO data) {
        ESystemUser currentUser = getCurrentUser();
        ERequest request = requestService.findByRequestIdentifier(data.getRequestId());
        service.processPayment(request, data.getPaymentReceived(), currentUser, data.getComment());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<Page<EPaymentDTO>> all() {
        Pageable pageable = new PageRequest(0, Integer.MAX_VALUE, new Sort(Sort.Direction.DESC, "paymentBookedAt"));
        Page<EPayment> entities = service.findAll(pageable);
        Page<EPaymentDTO> result = mapper.map(entities, EPaymentDTO.class);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(path = "/forRequest", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<Page<EPaymentDTO>> forRequest(@RequestParam("requestIdentifier") String requestIdentifier) {
        Pageable pageable = new PageRequest(0, Integer.MAX_VALUE, new Sort(Sort.Direction.DESC, "paymentBookedAt"));
        Page<EPayment> entities = service.findForRequest(requestIdentifier, pageable);
        Page<EPaymentDTO> result = mapper.map(entities, EPaymentDTO.class);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    private ESystemUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            ESystemUser principal = (ESystemUser) authentication.getPrincipal();
            return principal;
        }
        throw new AuthenticationCredentialsNotFoundException("could not get user");
    }
}
