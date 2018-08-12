package de.florianschmitt.rest.admin

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin")
class PingController {

    @GetMapping(path = ["/ping"])
    @ResponseStatus(HttpStatus.OK)
    fun ping() = {
    }

    @GetMapping(path = ["/getLoggedInName"], produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun getName() = GetNameResult(Helper.currentUser.germanDisplayString)

    @GetMapping(path = ["/getLoggedInRoles"], produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun getRoles(): GetRolesResult {
        val user = Helper.currentUser
        return GetRolesResult(user.hasFinanceRight, user.hasAdminRight)
    }
}

data class GetNameResult(val name: String) {
    constructor() : this("") // needed for JSON deserialization
}

data class GetRolesResult(val hasFinanceRight: Boolean, val hasAdminRight: Boolean) {
    constructor() : this(false, false) // needed for JSON deserialization
}
