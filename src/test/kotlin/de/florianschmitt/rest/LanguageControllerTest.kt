package de.florianschmitt.rest

import com.github.springtestdbunit.annotation.DatabaseOperation
import com.github.springtestdbunit.annotation.DatabaseSetup
import de.florianschmitt.base.BaseRestTest
import de.florianschmitt.base.DBUnitData
import org.junit.Test
import org.springframework.web.client.getForObject
import java.util.*
import kotlin.test.assertEquals

class LanguageControllerTest : BaseRestTest() {

    @Test
    @DatabaseSetup(value = DBUnitData.BASE, type = DatabaseOperation.CLEAN_INSERT)
    fun testGetLanguageGerman() {
        val languageResult = restTemplate.getForObject<List<LinkedHashMap<*, *>>>(buildUrl("getLanguages"))
        assertEquals(3, languageResult?.size)
        assertEquals("Arabisch", languageResult?.get(0)?.get("label"))
    }

    @Test
    @DatabaseSetup(value = DBUnitData.BASE, type = DatabaseOperation.CLEAN_INSERT)
    fun testGetLanguageArabic() {
        val languageResult = restTemplate.getForObject<List<LinkedHashMap<*, *>>>(buildUrl("/getLanguages?locale={locale}"), "ar")
        assertEquals(3, languageResult?.size)
        assertEquals("العربية", languageResult?.get(0)?.get("label"))
    }
}
