package de.florianschmitt.rest;

import java.util.LinkedHashMap;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;

import de.florianschmitt.rest.base.BaseRestTest;
import de.florianschmitt.rest.base.DBUnitData;

@SuppressWarnings("unchecked")
public class LanguageControllerTest extends BaseRestTest {

    @Test
    @DatabaseSetup(value = DBUnitData.BASE, type = DatabaseOperation.CLEAN_INSERT)
    public void testGetLanguageGerman() {
        List<LinkedHashMap<?, ?>> languageResult = restTemplate.getForObject(buildUrl("getLanguages"), List.class);
        Assert.assertThat(languageResult, Matchers.hasSize(3));
        Assert.assertThat(languageResult.get(0).get("label"), Matchers.equalTo("Arabisch"));
    }

    @Test
    @DatabaseSetup(value = DBUnitData.BASE, type = DatabaseOperation.CLEAN_INSERT)
    public void testGetLanguageArabic() {
        List<LinkedHashMap<?, ?>> languageResult = restTemplate.getForObject(buildUrl("/getLanguages?locale={locale}"),
                List.class, "ar");
        Assert.assertThat(languageResult, Matchers.hasSize(3));
        Assert.assertThat(languageResult.get(0).get("label"), Matchers.equalTo("العربية"));
    }
}
