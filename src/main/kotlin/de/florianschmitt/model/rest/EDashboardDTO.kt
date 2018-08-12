package de.florianschmitt.model.rest

import java.io.Serializable

class EDashboardDTO(
        var openRequests: Long = 0,
        var acceptedRequests: Long = 0,
        var canceledRequests: Long = 0,
        var finishedRequests: Long = 0,
        var expiredRequests: Long = 0
) : Serializable
