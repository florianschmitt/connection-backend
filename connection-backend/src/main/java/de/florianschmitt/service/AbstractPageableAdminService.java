package de.florianschmitt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.Serializable;

public abstract class AbstractPageableAdminService<ENTITY extends Serializable, REPOSITORY extends PagingAndSortingRepository<ENTITY, Long>> extends AbstractAdminService<ENTITY, REPOSITORY> implements BasePageableAdminService<ENTITY> {

    @Autowired
    protected REPOSITORY repository;

    @Override
    public Page<ENTITY> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
