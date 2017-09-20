package de.florianschmitt.model.entities

import javax.persistence.MappedSuperclass

import org.springframework.data.jpa.domain.AbstractPersistable

@MappedSuperclass
abstract class BaseEntity : AbstractPersistable<Long>() {

    /**
     * needs to be public, so it is setable from rest dto mapper.
     */
    public override fun setId(id: Long?) {
        super.setId(id)
    }
}
