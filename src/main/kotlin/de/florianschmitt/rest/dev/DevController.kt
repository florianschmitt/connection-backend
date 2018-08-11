package de.florianschmitt.rest.dev

import com.google.common.collect.Sets
import de.florianschmitt.model.entities.EOccasionEnum
import de.florianschmitt.model.entities.ERequest
import de.florianschmitt.service.LanguageService
import de.florianschmitt.service.RequestService
import de.florianschmitt.system.util.DevPostgresProfile
import de.florianschmitt.system.util.DevProfile
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.util.concurrent.ThreadLocalRandom

@RestController
@RequestMapping("/dev/")
@DevProfile
@DevPostgresProfile
internal class DevController {

    @Autowired
    private lateinit var requestService: RequestService

    @Autowired
    private lateinit var languageService: LanguageService

    @GetMapping(path = arrayOf("/createRequests"))
    @ResponseStatus(HttpStatus.OK)
    fun createRequests(@RequestParam(defaultValue = "10", name = "count") count: Int) {
        val languages = languageService.findAll(PageRequest.of(0, 10))

        for (it in 0..count) {
            val randomInt = ThreadLocalRandom.current().nextInt(languages.numberOfElements)
            val request = ERequest()
            request.requesterName = "Vor und Nachname"
            request.languages = Sets.newHashSet(languages.content[randomInt])
            request.datetime = LocalDateTime.now().plusDays(ThreadLocalRandom.current().nextInt(50).toLong())
            request.occasionEnum = EOccasionEnum.OTHER
            request.occasionString = "ein besonderer Anlass"
            request.city = "Koeln"
            request.postalCode = "12345"
            request.street = "Straßenname"
            request.email = "email@adresse.de"
            request.phone = "0123456"
            requestService.submitNewRequest(request)
        }
    }
}
