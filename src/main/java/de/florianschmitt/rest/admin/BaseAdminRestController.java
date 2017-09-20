package de.florianschmitt.rest.admin;

import de.florianschmitt.service.BaseAdminService;
import de.florianschmitt.service.BasePageableAdminService;
import de.florianschmitt.service.util.DTOMapper;
import de.florianschmitt.system.util.ReflectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.Optional;

@RestController
abstract class BaseAdminRestController<ENTITY extends AbstractPersistable<Long>, DTO extends Serializable, SERVICE extends BasePageableAdminService<ENTITY>> {

    @Autowired
    private SERVICE service;

    @Autowired
    private DTOMapper mapper;

    @RequestMapping(path = "/get/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<DTO> get(@PathVariable("id") long id) {
        Optional<ENTITY> element = service.findOne(id);

        ResponseEntity<DTO> result = element//
                .map(e -> mapper.map(e, getDtoClass()))//
                .map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))//
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        return result;
    }

    @RequestMapping(path = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<Page<DTO>> all() {
        Pageable pageable = new PageRequest(0, Integer.MAX_VALUE, getDefaultSortForAll());
        Page<ENTITY> entities = service.findAll(pageable);
        Page<DTO> result = mapper.map(entities, getDtoClass());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(path = "/save", method = {RequestMethod.POST})
    ResponseEntity<?> save(@RequestBody @Valid DTO dto) {
        ENTITY entity = mapper.map(dto, getEntityClass());
        service.save(entity);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/delete/{id}", method = {RequestMethod.DELETE})
    ResponseEntity<?> delete(@PathVariable("id") long id) {
        service.deleteOne(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    protected Sort getDefaultSortForAll() {
        return null;
    }

    protected final Class<DTO> getDtoClass() {
        return ReflectionUtils.classFromSecondGenericParameter(this);
    }

    protected final Class<ENTITY> getEntityClass() {
        return ReflectionUtils.classFromFirstGenericParameter(this);
    }
}
