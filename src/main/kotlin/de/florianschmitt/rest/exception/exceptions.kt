package de.florianschmitt.rest.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.OK, reason = "request already accepted")
class RequestAlreadyAcceptedException : RuntimeException()

@ResponseStatus(value = HttpStatus.OK, reason = "request is finished")
class RequestFinishedException : RuntimeException()

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "illegal request id")
class RequestNotFoundException : RuntimeException()

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "voucher was already answered")
class VoucherAlreadyAnsweredException : RuntimeException()

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "illegal voucher id")
class VoucherNotFoundException : RuntimeException()

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = FeedbackAlreadyGivenException.MSG)
class FeedbackAlreadyGivenException : RuntimeException() {
    companion object {
        const val MSG = "feedback already given"
    }
}

@ResponseStatus(value = HttpStatus.OK, reason = RequestNotYetFinishedException.MSG)
class RequestNotYetFinishedException : RuntimeException() {
    companion object {
        const val MSG = "request is not yet finished"
    }
}

@ResponseStatus(value = HttpStatus.OK, reason = RequestWasCanceledException.MSG)
class RequestWasCanceledException : RuntimeException() {
    companion object {
        const val MSG = "request was canceled"
    }
}

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = SystemMustHaveAtLeastASingleActiveAdmin.MSG)
class SystemMustHaveAtLeastASingleActiveAdmin : RuntimeException() {
    companion object {
        const val MSG = "system must have at least a single active admin"
    }
}