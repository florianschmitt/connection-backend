package de.florianschmitt.repository

import java.util.Optional

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param

import de.florianschmitt.model.entities.ELanguage
import de.florianschmitt.model.entities.ESystemUser
import de.florianschmitt.model.entities.EVolunteer

interface VolunteerRepository : PagingAndSortingRepository<EVolunteer, Long> {

    @Query(value = "Select Distinct x From EVolunteer x Join x.languages l Where x.isActive = true And l in :languages")
    fun findActiveVolunteerWhoHasLanguage(@Param(value = "languages") languages: Set<ELanguage>): Collection<EVolunteer>

    @Query("Select v From EVolunteer v Where v.id = :id")
    @EntityGraph(attributePaths = arrayOf("languages"))
    fun findOneWithLanguages(@Param("id") id: Long): Optional<EVolunteer>

    fun findByEmail(email: String): Optional<EVolunteer>
}
