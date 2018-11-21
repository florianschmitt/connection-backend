package de.florianschmitt.model.rest

import java.io.Serializable
import javax.validation.constraints.NotNull

class EFeedbackRequesterDTO(hasOccurred: Boolean,
                            wasCanceled: Boolean? = null,
                            wasPositive: Boolean? = null,
                            comment: String? = null) : Serializable {
    @NotNull
    var hasOccurred: Boolean = hasOccurred

    var wasCanceled: Boolean? = wasCanceled

    var wasPositive: Boolean? = wasPositive

    var comment: String? = comment
}
