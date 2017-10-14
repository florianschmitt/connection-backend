package de.florianschmitt.model.entities

enum class ERequestStateEnum(private val label: String) {
    OPEN("offen"),
    ACCEPTED("angenommen"),
    CANCELED("storniert"),
    FINISHED("abgeschlossen"),
    EXPIRED("verfallen"),
    ;

    override fun toString(): String {
        return label
    }
}
