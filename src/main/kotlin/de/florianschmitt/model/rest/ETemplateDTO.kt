package de.florianschmitt.model.rest

import java.io.Serializable

class ETemplateDTO(
        var id: Long? = null,
        var identifier: String? = null,
        var description: String? = null,
        var template: String? = null
) : Serializable