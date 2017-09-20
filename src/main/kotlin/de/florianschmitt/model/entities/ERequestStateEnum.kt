package de.florianschmitt.model.entities

enum class ERequestStateEnum(private val label: String) {
    OPEN("offen"), //
    ACCEPTED("angenommen"), //
    CANCELED("storniert"), //
    FINISHED("abgeschlossen");

    override fun toString(): String {
        return label
    }
}
