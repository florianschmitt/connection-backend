package de.florianschmitt.service.events

import de.florianschmitt.model.entities.ERequest
import de.florianschmitt.model.entities.ERequestStateEnum
import de.florianschmitt.model.entities.EVoucher
import org.springframework.context.ApplicationEvent

class RequestWasSubmittedEvent(source: Any, var request: ERequest, var vouchers: Collection<EVoucher>) : ApplicationEvent(source)

class RequestWasAcceptedEvent(source: Any, var request: ERequest) : ApplicationEvent(source)

class RequestWasCanceledEvent(source: Any, var request: ERequest, var previousState: ERequestStateEnum) : ApplicationEvent(source)

class RequestIsExpiredEvent(source: Any, var request: ERequest) : ApplicationEvent(source)