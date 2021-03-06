package de.florianschmitt.model.entities

enum class EOccasionEnum(private val label: String)
{
    DOCTOR("Arzt"),
    GOVERNMENT("Amt"),
    ATTORNEY("Anwalt"),
    RECREATION("Freizeitveranstaltung"),
    OTHER("Sonstiges"),
    ;

    override fun toString(): String {
        return label
    }
}