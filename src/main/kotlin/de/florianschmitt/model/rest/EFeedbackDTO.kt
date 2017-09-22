package de.florianschmitt.model.rest

import javax.validation.constraints.NotNull
import java.io.Serializable

class EFeedbackDTO(positive: Boolean = false, comment: String? = null) : Serializable {

    @NotNull
    var positive: Boolean = positive

    var comment: String? = comment
}
