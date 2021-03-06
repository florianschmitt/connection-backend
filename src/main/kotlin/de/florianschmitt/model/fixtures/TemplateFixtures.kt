package de.florianschmitt.model.fixtures

import de.florianschmitt.model.entities.ETemplate

enum class TemplateFixtures(
        val type: Type,
        val description: String,
        val filenameOrContent: String
) {
    REQUEST_ASK_VOLUNTEER_SUBJECT(Type.STRING, "Kopfzeile für Anfragemail an Freiwillige", "Anfrage für Sprachbegleitung"),
    REQUEST_ASK_VOLUNTEER_CONTENT(Type.FILE, "Vorlage für Anfragemail an Freiwillige", "requestAskVolunteer.txt"),

    REQUEST_CONFIRMATION_SUBJECT(Type.STRING, "Kopfzeile für Bestätigung der Anfrage an Anfragende", "Bestätigung für die Anfrage einer Sprachbegleitung"),
    REQUEST_CONFIRMATION_CONTENT(Type.FILE, "Vorlage für Bestätigung der Anfrage an Anfragende", "requestConfirmation.txt"),

    REQUEST_ACCEPTED_VOLUNTEER_SUBJECT(Type.STRING, "Kopfzeile für Bestätigungsmail an Freiwillige", "Bestätigung für die Übernahme einer Sprachbegleitung"),
    REQUEST_ACCEPTED_VOLUNTEER_CONTENT(Type.FILE, "Vorlage für Bestätigungsmail an Freiwillige", "requestAcceptedVolunteer.txt"),

    REQUEST_ACCEPTED_REQUESTER_SUBJECT(Type.STRING, "Kopfzeile für Bestätigung der Übernahme an Anfragende", "Bestätigung für die Übernahme einer Sprachbegleitung"),
    REQUEST_ACCEPTED_REQUESTER_CONTENT(Type.FILE, "Vorlage für Bestätigung der Übernahme an Anfragende", "requestAcceptedRequester.txt"),

    REQUEST_EXPIRED_REQUESTER_SUBJECT(Type.STRING, "Kopfzeile für Nachricht über nicht erfolgreiche Übernahme an Anfragende", "Es konnte kein Sprachbegleiter vermittelt werden"),
    REQUEST_EXPIRED_REQUESTER_CONTENT(Type.FILE, "Vorlage für Nachricht über nicht erfolgreiche Übernahme an Anfragende", "requestExpiredRequester.txt"),

    REQUEST_CANCELED_INFORM_VOLUNTEER_SUBJECT(Type.STRING, "Kopfzeile für Nachricht an Freiwillige, wenn Anfrage storniert wird", "Sprachbegleitung wurde abgesagt"),
    REQUEST_CANCELED_INFORM_VOLUNTEER_CONTENT(Type.FILE, "Vorlage für Nachricht an Freiwillige, wenn Anfrage storniert wird", "requestCanceledVolunteer.txt"),

    REQUEST_FINISHED_ASK_FEEDBACK_VOLUNTEER_SUBJECT(Type.STRING, "Kopfzeile für Nachricht an Freiwillige, wenn Sprachbegleitung abgeschlossen ist", "Sprachbegleitung hat stattgefunden"),
    REQUEST_FINISHED_ASK_FEEDBACK_VOLUNTEER_CONTENT(Type.FILE, "Vorlage für Nachricht an Freiwillige, wenn Sprachbegleitung abgeschlossen ist", "requestFinishedVolunteer.txt"),

    REQUEST_FINISHED_ASK_FEEDBACK_REQUESTER_SUBJECT(Type.STRING, "Kopfzeile für Nachricht an Anfragende, wenn Sprachbegleitung abgeschlossen ist", "Sprachbegleitung hat stattgefunden"),
    REQUEST_FINISHED_ASK_FEEDBACK_REQUESTER_CONTENT(Type.FILE, "Vorlage für Nachricht an Anfragende, wenn Sprachbegleitung abgeschlossen ist", "requestFinishedRequester.txt"),

    ;

    val identifier = this.name
    var instance: ETemplate? = null

    enum class Type {
        FILE, STRING
    }
}
