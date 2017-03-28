package de.florianschmitt.model.rest

import java.io.Serializable

class EDashboardDTO(
        var openRequests: Long,
        var acceptedRequests: Long,
        var canceledRequests: Long,
        var finishedRequests: Long
) : Serializable
