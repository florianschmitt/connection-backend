package de.florianschmitt.model.fixtures

import de.florianschmitt.model.entities.ETemplate

enum class TemplateFixtures(
        val type: Type,
        val description: String,
        val filenameOrContent: String
) {
    REQUEST_ASK_VOLUNTEER_SUBJECT(Type.STRING, "Kopfzeile für Anfragemail an freiwillige", "Anfrage für Sprachunterstützung"),
    REQUEST_ASK_VOLUNTEER_CONTENT(Type.FILE, "Vorlage für Anfragemail an freiwillige", "requestAskVolunteer.txt");

    val identifier = this.name
    var instance: ETemplate? = null

    enum class Type {
        FILE, STRING
    }
}
