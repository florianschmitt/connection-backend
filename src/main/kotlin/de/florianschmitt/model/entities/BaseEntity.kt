package de.florianschmitt.model.entities

import javax.persistence.MappedSuperclass

import org.springframework.data.jpa.domain.AbstractPersistable
import java.io.Serializable

@MappedSuperclass
abstract class BaseEntity : AbstractPersistable<Long>(), Serializable {

    /**
     * needs to be public, so it is setable from rest dto mapper.
     */
    public override fun setId(id: Long?) {
        super.setId(id)
    }
}
