package de.florianschmitt.service;

import java.io.Serializable;
import java.util.Optional;

public interface BaseAdminService<ENTITY extends Serializable> {
    void save(ENTITY volunteer);

    Iterable<ENTITY> findAll();

    Optional<ENTITY> findOne(long id);

    void deleteOne(long id);
}
