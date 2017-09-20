package de.florianschmitt.model.entities.util

import de.florianschmitt.model.entities.ELocalizedLanguageEnum
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter(autoApply = true)
class LocalizedLanguageEnumConverter : AttributeConverter<ELocalizedLanguageEnum, String> {

    override fun convertToDatabaseColumn(attribute: ELocalizedLanguageEnum): String {
        return attribute.name.toLowerCase()
    }

    override fun convertToEntityAttribute(dbData: String): ELocalizedLanguageEnum {
        return ELocalizedLanguageEnum.create(dbData)
    }
}
