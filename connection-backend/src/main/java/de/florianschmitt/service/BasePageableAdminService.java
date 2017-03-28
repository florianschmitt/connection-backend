package de.florianschmitt.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;

public interface BasePageableAdminService<ENTITY extends Serializable> extends BaseAdminService<ENTITY> {
    Page<ENTITY> findAll(Pageable pageable);
}
