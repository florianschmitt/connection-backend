package de.florianschmitt.model.fixtures

import de.florianschmitt.model.entities.ETemplate

enum class TemplateFixtures(
        val type: Type,
        val description: String,
        val filenameOrContent: String
) {
    REQUEST_ASK_VOLUNTEER_SUBJECT(Type.STRING, "Kopfzeile für Anfragemail an Freiwillige", "Anfrage für Sprachunterstützung"),
    REQUEST_ASK_VOLUNTEER_CONTENT(Type.FILE, "Vorlage für Anfragemail an Freiwillige", "requestAskVolunteer.txt"),

    REQUEST_ACCEPTED_VOLUNTEER_SUBJECT(Type.STRING, "Kopfzeile für Bestätigungsmail an Freiwillige", "Bestätigung für die Übernahme einer Sprachbegleitung"),
    REQUEST_ACCEPTED_VOLUNTEER_CONTENT(Type.FILE, "Vorlage für Bestätigungsmail an Freiwillige", "requestAcceptedVolunteer.txt"),

    REQUEST_ACCEPTED_REQUESTER_SUBJECT(Type.STRING, "Kopfzeile für Bestätigungsmail an Anfragende", "Bestätigung für die Übernahme einer Sprachbegleitung"),
    REQUEST_ACCEPTED_REQUESTER_CONTENT(Type.FILE, "Vorlage für Bestätigungsmail an Anfragende", "requestAcceptedRequester.txt"),
    ;

    val identifier = this.name
    var instance: ETemplate? = null

    enum class Type {
        FILE, STRING
    }
}
