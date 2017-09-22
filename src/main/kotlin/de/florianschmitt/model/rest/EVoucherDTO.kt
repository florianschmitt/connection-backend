package de.florianschmitt.model.rest

import java.io.Serializable

class EVoucherDTO(
        var id: Long? = null,
        var identifier: String? = null,
        var volunteerString: String? = null,
        var requestIdentifier: String? = null,
        var answer: Boolean? = null
) : Serializable