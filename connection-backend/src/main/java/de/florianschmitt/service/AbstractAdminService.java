package de.florianschmitt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Optional;

public abstract class AbstractAdminService<ENTITY extends Serializable, REPOSITORY extends CrudRepository<ENTITY, Long>> implements BaseAdminService<ENTITY> {

    @Autowired
    protected REPOSITORY repository;

    @Override
    @Transactional
    public void save(ENTITY entity) {
        repository.save(entity);
    }

    @Override
    public Iterable<ENTITY> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<ENTITY> findOne(long id) {
        return Optional.ofNullable(repository.findOne(id));
    }

    @Override
    @Transactional
    public void deleteOne(long id) {
        repository.delete(id);
    }
}
