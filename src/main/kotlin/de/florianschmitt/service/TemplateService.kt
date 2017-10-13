package de.florianschmitt.service

import com.google.common.io.Resources
import de.florianschmitt.model.entities.ETemplate
import de.florianschmitt.model.fixtures.TemplateFixtures
import de.florianschmitt.repository.TemplateRepository
import de.florianschmitt.service.base.AbstractAdminService
import org.springframework.beans.FatalBeanException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Service
import java.io.IOException
import java.nio.charset.StandardCharsets
import javax.annotation.PostConstruct

@Service
class TemplateService : AbstractAdminService<ETemplate, TemplateRepository>() {

    @Autowired
    private lateinit var resourceLoader: ResourceLoader

    @Cacheable //TODO:
    fun getTemplate(fixture: TemplateFixtures): String {
        return repository.findByIdentifier(fixture.identifier)
                .map { it.template }
                .orElseThrow { IllegalStateException() }
    }

    @PostConstruct
    fun addFixtures() {
        val identifierToEntityMap = repository.findAll().associate { it.identifier to it }

        for (fixture in TemplateFixtures.values()) {
            if (identifierToEntityMap.containsKey(fixture.identifier)) {
                fixture.instance = identifierToEntityMap[fixture.identifier]
            } else {
                createSaveAndSetEnumInstance(fixture)
            }
        }
    }

    private fun createSaveAndSetEnumInstance(fixture: TemplateFixtures) {
        var entity = create(fixture)
        entity = repository.save(entity)
        fixture.instance = entity
    }

    private fun create(fixture: TemplateFixtures): ETemplate {
        val content = if (fixture.type == TemplateFixtures.Type.FILE) {
            loadContentFromFile(fixture)
        } else {
            fixture.filenameOrContent
        }

        return ETemplate(fixture.identifier, fixture.description, content)
    }

    private fun loadContentFromFile(fixture: TemplateFixtures): String {
        val resource = resourceLoader.getResource("classpath:/templates/" + fixture.filenameOrContent)
        return try {
            Resources.toString(resource.url, StandardCharsets.UTF_8)
        } catch (e: IOException) {
            throw FatalBeanException("error loading file: " + fixture.filenameOrContent, e)
        }
    }
}
