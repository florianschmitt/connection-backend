package de.florianschmitt.model.rest

import javax.validation.constraints.NotNull
import java.io.Serializable

class EFeedbackDTO(positive: Boolean, comment: String? = null) : Serializable {

    constructor() : this(false, null)

    @NotNull
    var positive: Boolean = positive

    var comment: String? = comment
}
